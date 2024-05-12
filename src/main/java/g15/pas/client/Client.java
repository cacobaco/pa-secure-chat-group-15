package g15.pas.client;

import g15.pas.exceptions.*;
import g15.pas.message.Message;
import g15.pas.message.enums.CommandType;
import g15.pas.message.enums.InfoType;
import g15.pas.message.messages.*;
import g15.pas.utils.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

/**
 * The Client class represents a client in the system.
 * It holds the client's private and public keys, certificate, username, server host and port.
 * It also manages the connection to the server and the certificate authority.
 */
public class Client {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private BigInteger privateDHKey;
    private BigInteger publicDHKey;
    private final HashMap<String, BigInteger> sharedSecrets = new HashMap<>();

    private Certificate certificate;
    private final Map<String, Certificate> certificates = new HashMap<>();

    private final String username;
    private final String serverHost;
    private final int serverPort;
    private final String caHost;
    private final int caPort;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private PublicKey caPublicKey;

    private Thread serverListener;

    /**
     * Constructs a new Client with the specified username, server host, and server port.
     * It generates a key pair and a certificate for the client.
     *
     * @param username   the username of the client
     * @param serverHost the host of the server
     * @param serverPort the port of the server
     * @param caHost     the host of the certificate authority
     * @param caPort     the port of the certificate authority
     * @throws KeyPairCreationException if an error occurs while generating the key pair
     */
    public Client(String username, String serverHost, int serverPort, String caHost, int caPort) throws KeyPairCreationException {
        this.username = username;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.caHost = caHost;
        this.caPort = caPort;

        // Create key pair
        Logger.log("A gerar par de chaves...");
        KeyPair keyPair;
        try {
            keyPair = Encryption.generateKeyPair();
        } catch (Exception e) {
            throw new KeyPairCreationException(e);
        }
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        Logger.log("Par de chaves gerado com sucesso.");

        // Create Diffie-Hellman key pair
        Logger.log("A gerar par de chaves Diffie-Hellman...");
        try {
            this.privateDHKey = DiffieHellman.generatePrivateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyPairCreationException(e);
        }
        this.publicDHKey = DiffieHellman.generatePublicKey(this.privateDHKey);
        Logger.log("Par de chaves Diffie-Hellman gerado com sucesso.");

        // Create certificate
        Logger.log("A gerar certificado...");
        this.certificate = new Certificate(username, publicKey);
        Logger.log("Certificado gerado com sucesso.");
    }

    /**
     * Starts the client. Follows the steps of:<br>
     * 1. Connect to the certificate authority<br>
     * 2. Request a certificate signature<br>
     * 3. Disconnect from the certificate authority<br>
     * 4. Connect to the server<br>
     * 5. Send the certificate<br>
     * 6. Receive the certificate response<br>
     * 7. Start the server listener<br>
     * 8. Send messages to the server<br>
     * 9. Disconnect from the server<br>
     * <p>
     * If an error occurs during any of the steps, the client will disconnect from the server and stop.
     */
    public void start() {
        try {
            connectToCertificateAuthority();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao conectar à autoridade de certificação: " + e.getMessage());
            disconnectFromCertificateAuthority();
            return;
        }

        try {
            requestCAPublicKey();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao pedir chave pública da CA: " + e.getMessage());
            disconnectFromCertificateAuthority();
            return;
        }

        try {
            receiveCAPublicKey();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao receber chave pública da CA: " + e.getMessage());
            disconnectFromCertificateAuthority();
            return;
        }

        try {
            requestCertificateSignature();
        } catch (InvalidCertificateException e) {
            Logger.error("Ocorreu um erro ao pedir assinatura do certificado: " + e.getMessage());
            disconnectFromCertificateAuthority();
            return;
        }

        try {
            receiveCertificateSignature();
        } catch (InvalidCertificateException e) {
            Logger.error("Ocorreu um erro ao receber a assinatura do certificado: " + e.getMessage());
            disconnectFromCertificateAuthority();
            return;
        }

        disconnectFromCertificateAuthority();

        try {
            connectToServer();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao conectar ao servidor: " + e.getMessage());
            disconnectFromServer();
            return;
        }

        try {
            sendCertificate();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao enviar o certificado: " + e.getMessage());
            disconnectFromServer();
            return;
        }

        try {
            receiveCertificateResponse();
        } catch (InvalidCertificateException e) {
            Logger.error("Ocorreu um erro ao receber a resposta do certificado: " + e.getMessage());
            disconnectFromServer();
            return;
        }

        try {
            requestCertificates();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao pedir certificados: " + e.getMessage());
            disconnectFromServer();
            return;
        }

        serverListener = new Thread(new ServerListener());
        serverListener.start();

        try {
            Thread.sleep(1000); // Allow server listener to process some messages
        } catch (InterruptedException e) {
            Logger.error("Ocorreu um erro ao esperar: " + e.getMessage());
            disconnectFromServer();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        do {
            String message = scanner.nextLine();

            if (CertificateRevoker.isRevoked(certificate)) {
                Logger.error("Certificado revogado. Não é possível enviar mensagens.");
                break;
            }

            if (CertificateRevoker.isExpired(certificate)) {
                Logger.error("Certificado expirado. Não é possível enviar mensagens.");
                break;
            }

            try {
                sendMessage(message);
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar a mensagem: " + e.getMessage());
                break;
            }
        } while (true);

        disconnectFromServer();
    }

    /**
     * Stops the client.
     */
    public void stop() {
        disconnectFromServer();
    }

    /**
     * Connects the client to the certificate authority.
     *
     * @throws ConnectionException if an error occurs while connecting to the certificate authority
     */
    private void connectToCertificateAuthority() throws ConnectionException {
        try {
            Logger.log("A conectar à CA...");
            socket = new Socket(caHost, caPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Logger.log("Conexão à CA estabelecida com sucesso.");
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    private void requestCAPublicKey() throws ConnectionException {
        Logger.log("A pedir chave pública da CA...");

        CommandMessage commandMessage = new CommandMessage(CommandType.REQUEST_PUBLIC_KEY, username);
        sendMessage(commandMessage);

        Logger.log("Pedido de chave pública da CA enviado com sucesso.");
    }

    private void receiveCAPublicKey() throws ConnectionException {
        try {
            Logger.log("A receber chave pública da CA...");

            KeyMessage keyMessage = (KeyMessage) in.readObject();

            caPublicKey = (PublicKey) keyMessage.getContent();

            Logger.log("Chave pública da CA recebida com sucesso.");
        } catch (IOException | ClassNotFoundException e) {
            throw new ConnectionException(e);
        }
    }

    /**
     * Requests a certificate signature from the certificate authority.
     */
    private void requestCertificateSignature() throws InvalidCertificateException {
        try {
            Logger.log("A pedir assinatura do certificado...");

            Logger.log("A enviar certificado para assinatura...");

            CertificateWriter.writeCertificate(certificate, Config.CA_PATH + "/" + username + ".pem");

            Logger.log("Certificado enviado com sucesso.");

            CommandMessage commandMessage = new CommandMessage(CommandType.REQUEST_CERTIFICATE_SIGN, username);
            sendMessage(commandMessage);

            Logger.log("Pedido de assinatura do certificado enviado com sucesso.");
        } catch (Exception e) {
            throw new InvalidCertificateException(e);
        }
    }

    private void receiveCertificateSignature() throws InvalidCertificateException {
        try {
            Logger.log("A receber assinatura do certificado...");

            Logger.log("A receber resposta booleana da CA...");

            BooleanMessage responseMessage = (BooleanMessage) in.readObject();

            if (!responseMessage.getContent()) {
                throw new InvalidCertificateException();
            }

            Logger.log("Resposta booleana recebida com sucesso.");

            certificate = CertificateReader.readCertificate(Config.CA_PATH + "/" + username + ".pem");

            Logger.log("Assinatura do certificado recebida com sucesso.");
        } catch (Exception e) {
            throw new InvalidCertificateException(e);
        }
    }

    /**
     * Disconnects the client from the certificate authority.
     */
    private void disconnectFromCertificateAuthority() {
        try {
            Logger.log("A desconectar da autoridade de certificação...");

            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();

            Logger.log("Desconectado da autoridade de certificação.");
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao desconectar da autoridade de certificação: " + e.getMessage());
        }
    }

    /**
     * Connects the client to the server.
     *
     * @throws ConnectionException if an error occurs while connecting to the server
     */
    private void connectToServer() throws ConnectionException {
        try {
            Logger.log("A conectar ao servidor...");
            socket = new Socket(serverHost, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Logger.log("Conexão ao servidor estabelecida com sucesso.");
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    /**
     * Sends the client's certificate to the server.
     *
     * @throws ConnectionException if an error occurs while sending the certificate
     */
    private void sendCertificate() throws ConnectionException {
        Logger.log("A enviar certificado...");

        CertificateMessage certificateMessage = new CertificateMessage(certificate, username);
        sendMessage(certificateMessage);

        Logger.log("Certificado enviado com sucesso.");
    }

    /**
     * Receives the certificate response from the server.
     *
     * @throws InvalidCertificateException if the certificate is invalid or an error occurs while receiving the response
     */
    private void receiveCertificateResponse() throws InvalidCertificateException {
        try {
            Logger.log("A receber resposta do certificado...");

            BooleanMessage response = (BooleanMessage) in.readObject();

            if (!response.getContent()) {
                throw new InvalidCertificateException();
            }

            Logger.log("Certificado aceite pelo servidor.");
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidCertificateException(e);
        }
    }

    private void requestCertificates() throws ConnectionException {
        CommandMessage commandMessage = new CommandMessage(CommandType.REQUEST_CERTIFICATE, username);
        sendMessage(commandMessage);
    }

    private void requestDHKey(String recipient) throws ConnectionException {
        CommandMessage commandMessage = new CommandMessage(CommandType.REQUEST_PUBLIC_DH_KEY, username, new String[]{recipient});
        sendMessage(commandMessage);
    }

    /**
     * Sends an encrypted Diffie-Hellman key to a list of recipients.
     * This method is used when the client wants to establish a shared secret with multiple other clients.
     * It encrypts the client's public Diffie-Hellman key with the public key of each recipient and sends it to them.
     * The recipients can then decrypt the Diffie-Hellman key, compute the shared secret, and store it for future communication.
     *
     * @param recipient an usernames of the recipient
     * @throws Exception if an error occurs during the encryption or sending of the Diffie-Hellman key
     */
    private void sendEncryptedDHKey(String recipient) throws Exception {
        PublicKey recipientPublicKey = getRecipientPublicKey(recipient);

        if (recipientPublicKey == null) {
            return;
        }

        EncryptedDHKeyMessage encryptedDHKey = new EncryptedDHKeyMessage(Encryption.encryptRSA(publicDHKey.toByteArray(), recipientPublicKey), username, new String[]{recipient});

        out.writeObject(encryptedDHKey);
    }

    /**
     * Sends a text message to the server.
     *
     * @param message the message to send
     * @throws ConnectionException if an error occurs while sending the message
     */
    public void sendMessage(String message) throws ConnectionException {
        try {
            TextMessage textMessage = TextMessage.fromString(message, username);
            textMessage.format();

            String[] recipients;

            if (textMessage.getRecipients() != null) {
                recipients = textMessage.getRecipients();
            } else {
                recipients = certificates.keySet().toArray(new String[0]);
            }

            recipients = Arrays.stream(recipients).filter(recipient -> !recipient.equals(username)).toArray(String[]::new);

            for (String recipient : recipients) {
                try {
                    checkCertificateRevoke(recipient);
                } catch (InvalidCertificateException e) {
                    Logger.error(e.getMessage());
                    continue;
                }

                BigInteger sharedSecret = sharedSecrets.get(recipient);

                if (sharedSecret == null) {
                    sendEncryptedDHKey(recipient);

                    requestDHKey(recipient);

                    int maxTries = 10;
                    int tries = 0;
                    while (sharedSecrets.get(recipient) == null && tries < maxTries) {
                        Thread.sleep(100);
                        tries++;
                    }

                    if (sharedSecrets.get(recipient) == null) {
                        Logger.error("Não foi possível estabelecer uma chave partilhada com o cliente: " + recipient);
                        continue;
                    }
                }

                byte[] messageBytes = textMessage.getContent().getBytes(StandardCharsets.UTF_8);
                byte[] encryptedMessage = Encryption.encryptAES(messageBytes, sharedSecrets.get(recipient).toByteArray());

                PublicKey recipientPublicKey = getRecipientPublicKey(recipient);

                if (recipientPublicKey == null) {
                    continue;
                }

                byte[] mac = Integrity.generateMAC(messageBytes, sharedSecrets.get(recipient).toByteArray());
                EncryptedMessage encryptedMessageObject = new EncryptedMessage(encryptedMessage, mac, username, new String[]{recipient});
                out.writeObject(encryptedMessageObject);
                out.flush();
            }

            Logger.log(textMessage.getContent());
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
    }

    public PublicKey getRecipientPublicKey(String recipientUsername) {
        Certificate recipientCertificate = certificates.get(recipientUsername);

        if (recipientCertificate == null) {
            Logger.error("Não existe nenhum certificado para o cliente: " + recipientUsername);
            return null;
        }

        return recipientCertificate.getPublicKey();
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to send
     * @throws ConnectionException if an error occurs while sending the message
     */
    private void sendMessage(Message message) throws ConnectionException {
        if (message.getRecipients() != null) {
            List<String> recipients = new ArrayList<>(Arrays.stream(message.getRecipients()).toList());

            for (String recipient : recipients) {
                try {
                    checkCertificateRevoke(recipient);
                } catch (InvalidCertificateException e) {
                    recipients.remove(recipient);
                    Logger.error(e.getMessage());
                }
            }

            message.setRecipients(recipients.toArray(new String[0]));
        }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    private void checkCertificateRevoke(String username) throws InvalidCertificateException {
        Certificate certificate = certificates.get(username);

        if (certificate == null) {
            certificates.remove(username);
            sharedSecrets.remove(username);
            throw new InvalidCertificateException("Certificado não encontrado para o utilizador \"" + username + "\".");
        }

        if (CertificateRevoker.isRevoked(certificate)) {
            certificates.remove(username);
            sharedSecrets.remove(username);
            throw new InvalidCertificateException("Certificado revogado para o utilizador \"" + username + "\".");
        }

        if (CertificateRevoker.isExpired(certificate)) {
            certificates.remove(username);
            sharedSecrets.remove(username);
            throw new InvalidCertificateException("Certificado expirado para o utilizador \"" + username + "\".");
        }
    }

    /**
     * Disconnects the client from the server.
     */
    private void disconnectFromServer() {
        try {
            Logger.log("A desconectar do servidor...");

            if (serverListener != null) serverListener.interrupt();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();

            Logger.log("Desconectado do servidor com sucesso.");
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao desconectar do servidor: " + e.getMessage());
        }
    }

    /**
     * The ServerListener class listens for messages from the server and handles them.
     */
    private class ServerListener implements Runnable {

        @Override
        public void run() {
            Date date = new Date();
            Logger.log("[%s] O utilizador \"%s\" ligou-se ao chat.", date, username);

            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    handleMessage(message);
                } catch (IOException | ClassNotFoundException e) {
                    Logger.error("Ocorreu um erro ao receber uma mensagem: " + e.getMessage());
                    break;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            disconnectFromServer();
        }

        private void handleMessage(Message message) throws Exception {
            String sender = message.getSender();

            if (sender != null) {
                if (sender.equals(username)) {
                    return;
                }

                if (!(message instanceof CertificateMessage)) {
                    try {
                        checkCertificateRevoke(sender);
                    } catch (InvalidCertificateException e) {
                        Logger.error(e.getMessage());
                        return;
                    }
                }
            }

            if (message instanceof CertificateMessage certificateMessage) {
                try {
                    handleCertificateMessage(certificateMessage);
                } catch (InvalidCertificateException e) {
                    Logger.error("Ocorreu um erro ao processar o certificado: " + e.getMessage());
                }
            } else if (message instanceof CommandMessage commandMessage) {
                try {
                    handleCommandMessage(commandMessage);
                } catch (InvalidCommandException e) {
                    Logger.error("Ocorreu um erro ao processar o comando: " + e.getMessage());
                }
            } else if (message instanceof TextMessage textMessage) {
                handleTextMessage(textMessage);
            } else if (message instanceof EncryptedMessage encryptedMessage) {
                handleEncryptedMessage(encryptedMessage);
            } else if (message instanceof EncryptedDHKeyMessage encryptedDHKeyMessage) {
                handleEncryptedDHKeyMessage(encryptedDHKeyMessage);
            } else if (message instanceof InfoMessage infoMessage) {
                try {
                    handleInfoMessage(infoMessage);
                } catch (InvalidInfoException e) {
                    Logger.error("Ocorreu um erro ao processar a informação: " + e.getMessage());
                }
            } else {
                Logger.error("Mensagem inválida recebida: %s" + message.getClass().getSimpleName());
            }
        }

        private void handleEncryptedDHKeyMessage(EncryptedDHKeyMessage message) throws Exception {
            sharedSecrets.put(message.getSender(), DiffieHellman.computeSecret(new BigInteger(Encryption.decryptRSA(message.getContent(), privateKey)), privateDHKey));
        }

        private void handleCertificateMessage(CertificateMessage certificateMessage) throws InvalidCertificateException {
            Certificate receivedCertificate = certificateMessage.getContent();
            validateCertificate(receivedCertificate);
            certificates.put(receivedCertificate.getUsername(), receivedCertificate);
        }

        private void handleCommandMessage(CommandMessage commandMessage) throws InvalidCommandException {
            if (commandMessage.getContent() == CommandType.REQUEST_CERTIFICATE) {
                String[] recipients = new String[]{commandMessage.getSender()};
                CertificateMessage response = new CertificateMessage(certificate, username, recipients);

                try {
                    sendMessage(response);
                } catch (ConnectionException e) {
                    Logger.error("Ocorreu um erro ao enviar o certificado: " + e.getMessage());
                }
            } else if (commandMessage.getContent() == CommandType.REQUEST_PUBLIC_DH_KEY) {
                try {
                    sendEncryptedDHKey(commandMessage.getSender());
                } catch (Exception e) {
                    Logger.error("Ocorreu um erro ao enviar a chave Diffie-Hellman: " + e.getMessage());
                }
            } else {
                throw new InvalidCommandException("Comando inválido recebido: %s" + commandMessage.getContent());
            }
        }

        private void handleTextMessage(TextMessage textMessage) {
            textMessage.format();
            Logger.log(textMessage.getContent());
        }

        /**
         * Handles an encrypted message received from the server.
         * The method decrypts the message using the shared secret associated with the sender of the message.
         * It also decrypts the signature of the message and verifies its integrity by comparing it with the digest of the decrypted message.
         * If the integrity check passes, it logs the decrypted message. If it fails, it throws a RuntimeException.
         *
         * @param EncryptedMessage the encrypted message to handle
         * @throws Exception if an error occurs during decryption or integrity verification
         */
        private void handleEncryptedMessage(EncryptedMessage EncryptedMessage) throws Exception {
            byte[] decryptedMessage = Encryption.decryptAES(EncryptedMessage.getContent(), sharedSecrets.get(EncryptedMessage.getSender()).toByteArray());
            byte[] mac = Integrity.generateMAC(decryptedMessage, sharedSecrets.get(EncryptedMessage.getSender()).toByteArray());
            // Verifies the integrity of the message
            boolean isIntegrity = Integrity.verifyMAC(mac, EncryptedMessage.getMac());
            if (!isIntegrity) {
                throw new RuntimeException("Integrity violation");
            } else {
                String messageString = new String(decryptedMessage, StandardCharsets.UTF_8);
                Logger.log(messageString);
            }
        }

        private void handleInfoMessage(InfoMessage infoMessage) throws InvalidInfoException {
            if (infoMessage.getContent() == InfoType.LOGOUT) {
                certificates.remove(infoMessage.getSender());
                sharedSecrets.remove(infoMessage.getSender());

                Date date = new Date();
                Logger.log("[%s] O utilizador \"%s\" desligou-se do chat.", date, infoMessage.getSender());
            } else {
                throw new InvalidInfoException("Info inválida recebida: %s" + infoMessage.getContent());
            }
        }

        /**
         * Validates a given certificate.
         */
        private void validateCertificate(Certificate certificate) throws InvalidCertificateException {
            try {
                Logger.log("A validar certificado de \"%s\"...", certificate.getUsername());

                boolean valid = CertificateSigner.verifyCertificate(certificate, caPublicKey);

                if (!valid) {
                    throw new InvalidCertificateException("Certificado não passou na validação.");
                }

                Logger.log("Certificado de \"%s\" validado com sucesso.", certificate.getUsername());

                Date date = new Date();
                Logger.log("[%s] O utilizador \"%s\" ligou-se ao chat.", date, certificate.getUsername());
            } catch (Exception e) {
                throw new InvalidCertificateException("Certificado não passou na validação.", e);
            }
        }

    }

}
