package g15.pas.client;

import g15.pas.exceptions.ConnectionException;
import g15.pas.exceptions.InvalidCertificateException;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Certificate;
import g15.pas.utils.Encryption;
import g15.pas.utils.Logger;
import g15.pas.utils.Message;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

/**
 * The Client class represents a client in the system.
 * It holds the client's private and public keys, certificate, username, server host and port.
 * It also manages the connection to the server and the certificate authority.
 */
public class Client {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final Certificate certificate;

    private final String username;
    private final String serverHost;
    private final int serverPort;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread serverListener;

    private Socket socketCA;
    private ObjectOutputStream outCA;
    //private final ObjectInputStream inCA;
    private final String caHost;
    private final int caPort;


    /**
     * Constructs a new Client with the specified username, server host, and server port.
     * It generates a key pair and a certificate for the client.
     *
     * @param username   the username of the client
     * @param serverHost the host of the server
     * @param serverPort the port of the server
     * @throws KeyPairCreationException if an error occurs while generating the key pair
     */
    public Client(String username, String serverHost, int serverPort ,String caHost, int caPort) throws KeyPairCreationException {
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
            try {
                disconnectFromCertificateAuthority();
            } catch (ConnectionException ex) {
                Logger.error("Ocorreu um erro ao desconectar da autoridade de certificação: " + ex.getMessage());
            }
            return;
        }

        requestCertificateSignature();

        try {
            disconnectFromCertificateAuthority();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao desconectar da autoridade de certificação: " + e.getMessage());
            return;
        }

        try {
            connectToServer();
        } catch (ConnectionException e) {
            Logger.error("Ocorreu um erro ao conectar ao servidor: " + e.getMessage());
            disconnectFromServer();
            return;
        }

        try {
            sendCertificate(certificate,"CertificateStorage");
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

        serverListener = new Thread(new ServerListener());
        serverListener.start();

        Scanner scanner = new Scanner(System.in);

        do {
            String message = scanner.nextLine();

            if (!sendMessage(message)) {
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
        // TODO implement
    }

    /**
     * Requests a certificate signature from the certificate authority.
     */
    private void requestCertificateSignature() {
        // TODO implement
    }

    /**
     * Disconnects the client from the certificate authority.
     *
     * @throws ConnectionException if an error occurs while disconnecting from the certificate authority
     */
    private void disconnectFromCertificateAuthority() throws ConnectionException {
        // TODO implement
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

    private void connectToCA() throws ConnectionException {
        try {
            Logger.log("A conectar ao certificado de autenticacao...");

            socketCA = new Socket(caHost, caPort);
            outCA = new ObjectOutputStream(socketCA.getOutputStream());
            //this.inCA = new ObjectInputStream(socketCA.getInputStream());
            Logger.log("Conexão ao servidor estabelecida com sucesso.");

            /*
            socket = new Socket(serverHost, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Logger.log("Conexão ao servidor estabelecida com sucesso.");*/
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    /**
     * Sends the client's certificate to the server.
     *
     * @throws ConnectionException if an error occurs while sending the certificate
     */
    private void sendCertificate(Certificate certificate,String folderPath) throws ConnectionException {

            Logger.log("A enviar certificado...");
            //out.writeObject(certificate);
            try {
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs(); // Create the folder if it doesn't exist
                }

                File certificateFile = new File(folder, "certificate" + username + ".txt");

                StringBuilder certificateContent = new StringBuilder();
                certificateContent.append("Username: ").append(username).append("\n");
                certificateContent.append("Public Key: ").append(publicKey).append("\n");
                certificateContent.append("hash: ").append(certificate.getAdditionalInfo()).append("\n");


                try (FileWriter writer = new FileWriter(certificateFile)) {
                    writer.write(certificateContent.toString());
                }


                System.out.println("Certificado salvo: " + certificateFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving certificate to folder: " + e.getMessage());
            }


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

            Boolean response = (Boolean) in.readObject();

            if (!response) {
                throw new InvalidCertificateException();
            }

            Logger.log("Certificado aceite pelo servidor.");
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidCertificateException(e);
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to send
     * @return true if the message was sent successfully, false otherwise
     */
    private boolean sendMessage(String message) {
        try {
            Message messageObject = Message.fromString(message, username);
            out.writeObject(messageObject);
            return true;
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao enviar uma mensagem: " + e.getMessage());
            disconnectFromServer();
            return false;
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
            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    message.format();
                    Logger.log(message.getContent());
                } catch (IOException | ClassNotFoundException e) {
                    Logger.error("Ocorreu um erro ao receber uma mensagem do servidor: " + e.getMessage());
                    break;
                }
            }

            // TODO implement disconnect
        }

        /**
         * Validates a given certificate.
         */
        private void validateCertificate(Certificate certificate) {
            // TODO implement
        }

    }

}
