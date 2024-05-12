package g15.pas.client;

import g15.pas.exceptions.ConnectionException;
import g15.pas.exceptions.InvalidCertificateException;
import g15.pas.exceptions.InvalidCommandException;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.message.Message;
import g15.pas.message.enums.CommandType;
import g15.pas.message.messages.*;
import g15.pas.utils.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Client class represents a client in the system.
 * It holds the client's private and public keys, certificate, username, server host and port.
 * It also manages the connection to the server and the certificate authority.
 */
public class Client {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private Certificate certificate;

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
        } catch (NoSuchAlgorithmException e) {
            throw new KeyPairCreationException(e);
        }
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        Logger.log("Par de chaves gerado com sucesso.");

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

        Scanner scanner = new Scanner(System.in);

        do {
            String message = scanner.nextLine();

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

    /**
     * Sends a text message to the server.
     *
     * @param message the message to send
     * @throws ConnectionException if an error occurs while sending the message
     */
    private void sendMessage(String message) throws ConnectionException {
        TextMessage textMessageObject = TextMessage.fromString(message, username);
        sendMessage(textMessageObject);
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to send
     * @throws ConnectionException if an error occurs while sending the message
     */
    private void sendMessage(Message message) throws ConnectionException {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new ConnectionException(e);
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

        private final Map<String, Certificate> certificates = new HashMap<>();

        @Override
        public void run() {
            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    handleMessage(message);
                } catch (IOException | ClassNotFoundException e) {
                    Logger.error("Ocorreu um erro ao receber uma mensagem: " + e.getMessage());
                    break;
                }
            }

            disconnectFromServer();
        }

        private void handleMessage(Message message) {
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
            } else {
                Logger.error("Mensagem inválida recebida: %s" + message.getClass().getSimpleName());
            }
        }

        private void handleCertificateMessage(CertificateMessage certificateMessage) throws InvalidCertificateException {
            if (username.equals(certificateMessage.getSender())) return;

            Certificate receivedCertificate = certificateMessage.getContent();
            validateCertificate(receivedCertificate);
            certificates.put(receivedCertificate.getUsername(), receivedCertificate);
        }

        private void handleCommandMessage(CommandMessage commandMessage) throws InvalidCommandException {
            if (username.equals(commandMessage.getSender())) return;

            if (commandMessage.getContent() == CommandType.REQUEST_CERTIFICATE) {
                String[] recipients = new String[]{commandMessage.getSender()};
                CertificateMessage response = new CertificateMessage(certificate, username, recipients);

                try {
                    sendMessage(response);
                } catch (ConnectionException e) {
                    Logger.error("Ocorreu um erro ao enviar o certificado: " + e.getMessage());
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
            } catch (Exception e) {
                throw new InvalidCertificateException("Certificado não passou na validação.", e);
            }
        }

    }

}
