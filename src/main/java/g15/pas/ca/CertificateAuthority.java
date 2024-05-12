package g15.pas.ca;

import g15.pas.exceptions.ConnectionException;
import g15.pas.exceptions.InvalidCertificateException;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.message.Message;
import g15.pas.message.enums.CommandType;
import g15.pas.message.messages.BooleanMessage;
import g15.pas.message.messages.CommandMessage;
import g15.pas.message.messages.KeyMessage;
import g15.pas.utils.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CertificateAuthority {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final ServerSocket caSocket;

    /**
     * Constructor to initialize the Certificate Authority.
     *
     * @param port The port number on which the CA will listen for connections.
     * @throws IOException              If an I/O error occurs when opening the server socket.
     * @throws KeyPairCreationException If an error occurs while generating the key pair.
     */
    public CertificateAuthority(int port) throws IOException, KeyPairCreationException {
        // Create key pair
        System.out.println("A gerar par de chaves...");
        KeyPair keyPair;
        try {
            keyPair = Encryption.generateKeyPair();
        } catch (Exception e) {
            throw new KeyPairCreationException("Erro ao criar o par de chaves: " + e.getMessage(), e);
        }
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        System.out.println("Par de chaves gerado com sucesso.");

        // Start CA
        Logger.log("A iniciar CA...");
        this.caSocket = new ServerSocket(port);
        Logger.log("CA iniciada com sucesso na porta \"%d\".", port);
    }

    /**
     * Starts the Certificate Authority, accepting client connections.
     */
    public void start() {
        Logger.log("CA à espera de conexões...");

        while (true) {
            try {
                Socket socket = caSocket.accept();
                Logger.log("Nova conexão aceite.");
                createClientHandler(socket);
            } catch (IOException e) {
                Logger.error("Erro ao aceitar conexão: " + e.getMessage());
                break;
            }
        }

        close();
    }

    /**
     * Stops the Certificate Authority.
     */
    public void stop() {
        close();
    }

    /**
     * Closes the CA server socket.
     */
    private void close() {
        try {
            Logger.log("A fechar CA...");
            caSocket.close();
            Logger.log("CA fechada com sucesso.");
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao fechar a CA: " + e.getMessage());
        }
    }

    /**
     * Creates a new client handler for the given socket.
     *
     * @param socket The socket representing the client connection.
     */
    private void createClientHandler(Socket socket) {
        try {
            Logger.log("A criar handler para o cliente...");
            ClientHandler clientHandler = new ClientHandler(socket);
            clientHandler.start();
            Logger.log("Handler criado com sucesso.");
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao criar handler para o cliente: " + e.getMessage());
        }
    }

    /**
     * Represents a client connection to the Certificate Authority.
     */
    private class ClientHandler extends Thread {

        private final Socket socket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private String username;

        /**
         * Constructor to initialize the client handler.
         *
         * @param socket The socket representing the client connection.
         * @throws IOException If an I/O error occurs when creating the input or output streams.
         */
        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        }

        /**
         * Runs the client handler thread, handling incoming messages from the client.
         */
        @Override
        public void run() {
            while (true) {
                try {
                    Message message = (Message) in.readObject();

                    if (username == null) {
                        username = message.getSender();
                    }

                    handleMessage(message);
                } catch (IOException | ClassNotFoundException e) {
                    Logger.error("Ocorreu um erro ao ler mensagem do cliente: " + e.getMessage());
                    break;
                }
            }

            closeConnection();
        }

        /**
         * Closes the client connection.
         */
        private void closeConnection() {
            try {
                Logger.log("A fechar conexão...");

                in.close();
                out.close();
                socket.close();

                Logger.log("Conexão fechada com sucesso.");
            } catch (IOException e) {
                Logger.error("Ocorreu um erro ao fechar conexão: " + e.getMessage());
            } finally {
                this.interrupt();
            }
        }

        /**
         * Handles an incoming message from the client.
         *
         * @param message The message received from the client.
         */
        private void handleMessage(Message message) {
            Logger.log("A receber mensagem...");

            if (message instanceof CommandMessage commandMessage) {
                if (commandMessage.getContent() == CommandType.REQUEST_PUBLIC_KEY) {
                    handlePublicKeyRequest();
                } else if (commandMessage.getContent() == CommandType.REQUEST_CERTIFICATE_SIGN) {
                    handleSignRequest();
                } else {
                    Logger.error("Comando inválido.");
                }
            } else {
                Logger.error("Mensagem inválida.");
            }

            Logger.log("Mensagem recebida de \"%s\": \"%s\"", message.getSender(), message.getContent());
        }

        /**
         * Handles a public key request from the client.
         */
        private void handlePublicKeyRequest() {
            try {
                sendPublicKey();
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar chave pública: " + e.getMessage());
                closeConnection();
            }
        }

        /**
         * Sends the CA's public key to the client.
         *
         * @throws ConnectionException If an error occurs while sending the public key.
         */
        private void sendPublicKey() throws ConnectionException {
            Logger.log("A enviar chave pública...");

            KeyMessage keyMessage = new KeyMessage(publicKey);
            sendMessage(keyMessage);

            Logger.log("Chave pública enviada com sucesso.");
        }

        /**
         * Handles a certificate signing request from the client.
         */
        private void handleSignRequest() {
            try {
                signCertificate(username);
            } catch (InvalidCertificateException e) {
                Logger.error("Ocorreu um erro ao assinar o certificado: " + e.getMessage());
                try {
                    sendSignResponse(false);
                } catch (ConnectionException ex) {
                    Logger.error("Ocorreu um erro ao enviar resposta do certificado: " + ex.getMessage());
                } finally {
                    closeConnection();
                }
                return;
            }

            try {
                sendSignResponse(true);
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar resposta do certificado: " + e.getMessage());
                closeConnection();
                return;
            }
        }

        /**
         * Signs a certificate for the specified username.
         *
         * @param username The username for which to sign the certificate.
         * @throws InvalidCertificateException If an error occurs while signing the certificate.
         */
        private void signCertificate(String username) throws InvalidCertificateException {
            Logger.log("A assinar certificado para \"%s\"...", username);

            Certificate certificate;

            try {
                certificate = CertificateReader.readCertificate(Config.CA_PATH + "/" + username + ".pem");
                certificate = CertificateSigner.signCertificate(certificate, privateKey);
                CertificateWriter.writeCertificate(certificate, Config.CA_PATH + "/" + username + ".pem");
            } catch (Exception e) {
                throw new InvalidCertificateException(e);
            }

            Logger.log("Certificado assinado com sucesso.");
        }

        /**
         * Sends a response to the client indicating the status of the certificate signing request.
         *
         * @param response The response status (true for success, false for failure).
         * @throws ConnectionException If an error occurs while sending the response.
         */
        private void sendSignResponse(boolean response) throws ConnectionException {
            Logger.log("A enviar resposta de assinatura de certificado...");

            BooleanMessage responseMessage = new BooleanMessage(response);
            sendMessage(responseMessage);

            Logger.log("Resposta de assinatura de certificado enviada com sucesso.");
        }

        /**
         * Sends a message to the client.
         *
         * @param message The message to be sent.
         * @throws ConnectionException If an error occurs while sending the message.
         */
        public void sendMessage(Message message) throws ConnectionException {
            synchronized (out) {
                try {
                    Logger.log("A enviar mensagem para \"%s\": \"%s\"", username, message.getContent());
                    out.writeObject(message);
                    out.flush();
                    Logger.log("Mensagem enviada com sucesso.");
                } catch (IOException e) {
                    throw new ConnectionException(e);
                }
            }
        }
    }
}




