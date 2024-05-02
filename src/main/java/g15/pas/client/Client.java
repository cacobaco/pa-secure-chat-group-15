package g15.pas.client;


import g15.pas.utils.*;
import g15.pas.client.exceptions.KeyPairCreationException;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Scanner;

public class Client {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final Certificate certificate;

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final String username;
    private Thread serverListener;
    private final PublicKey publicRSAKey;
    private final PrivateKey privateRSAKey;
    private final PublicKey receiverPublicRSAKey;
    private final BigInteger privateDHKey;
    private final BigInteger publicDHKey;



    public Client(String serverHost, int serverPort, String username) throws KeyPairCreationException, IOException {
        // Create key pair
        System.out.println("A gerar par de chaves...");
        KeyPair keyPair;
        try {
            keyPair = Encryption.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyPairCreationException("Erro ao criar o par de chaves: " + e.getMessage(), e);
        }
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        System.out.println("Par de chaves gerado com sucesso.");

        // Create certificate
        System.out.println("A gerar certificado...");
        this.certificate = new Certificate(username, publicKey);
        System.out.println("Certificado gerado com sucesso.");

        this.socket = new Socket(serverHost, serverPort);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        KeyPair keyPair = Encryption.generateKeyPair();
        this.privateRSAKey = keyPair.getPrivate();
        this.publicRSAKey = keyPair.getPublic();
        this.privateDHKey = DiffieHellman.generatePrivateKey();
        this.publicDHKey = DiffieHellman.generatePublicKey(this.privateDHKey);
        receiverPublicRSAKey = rsaKeyDistribution(); // TODO ir buscar a chave publica à CA
        this.username = username;
        System.out.println("Conectado ao servidor.");
    }

    public void start() throws Exception {
        System.out.println("A enviar nome de utilizador...");
        if (!sendUsernameMessage()) {
            close();
            return;
        }

        serverListener = new Thread(new ServerListener());
        serverListener.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            Message messageObj = new Message(username, message);

            // Encrypt the message
            BigInteger sharedSecret = agreeOnSharedSecret(messageObj.getRecipients());
            byte[] encryptedMessage = Encryption.encryptAES(messageObj.getMessage().getBytes(), sharedSecret.toByteArray());
            byte[] signature = Encryption.encryptRSA(messageObj.getMessage().getBytes(), privateRSAKey);
            // Store the encrypted message and the signature in an EncryptedMessage object
            EncryptedMessage encryptedMessageObj = new EncryptedMessage(encryptedMessage, signature, messageObj.getRecipients(), messageObj.getSender());

            try {
                out.writeObject(encryptedMessageObj);
            } catch (IOException e) {
                System.err.println("Ocorreu um erro ao enviar a mensagem: " + e.getMessage());
                System.out.println("A fechar cliente...");
                close();
                System.out.println("Cliente fechado.");
                break;
            }
        }
        close();
    }

    private void close() {
        try {
            System.out.println("A fechar cliente...");
            serverListener.interrupt();
            in.close();
            out.close();
            socket.close();
            System.out.println("Cliente fechado.");
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao fechar as streams e o socket: " + e.getMessage());
        }
    }

    private void sendPublicKey(BigInteger publicKey) throws Exception {
        out.writeObject(Encryption.encryptRSA(publicKey.toByteArray(), this.privateRSAKey));
    }


    private boolean sendUsernameMessage() {
        try {
            out.writeObject(username);

            Boolean response = (Boolean) in.readObject();

            if (!response) {
                System.err.println("Nome de utilizador inválido.");
                close();
                return false;
            }

            System.out.println("Nome de utilizador enviado com sucesso.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ocorreu um erro ao enviar o nome de utilizador: " + e.getMessage());
            close();
        }

        return false;
    }

    private BigInteger agreeOnSharedSecret(List<String> recipients) throws Exception {
        // Sends the public key to the receiver
        BigInteger clientPublicKeyEncrypted = new BigInteger((byte[]) in.readObject());
        // Decrypts the client's public key
        BigInteger clientPublicKey = new BigInteger(Encryption.decryptRSA(clientPublicKeyEncrypted.toByteArray(), receiverPublicRSAKey));
        sendPublicKey(publicDHKey);
        // Computes the shared secret
        return DiffieHellman.computeSecret(clientPublicKey, privateDHKey);
    }

    /**
     * Executes the key distribution protocol. The sender sends its public key to the receiver and receives the public
     * key of the receiver.
     *
     * @return the public key of the sender
     * @throws Exception when the key distribution protocol fails
     */
    private PublicKey rsaKeyDistribution() throws Exception { // TODO enviar a chave publica à CA
        // Sends the public key
        sendPublicRSAKey();
        // Receive the public key of the sender
        return (PublicKey) in.readObject();
    }

    /**
     * Sends the public key of the sender to the receiver.
     *
     * @throws IOException when an I/O error occurs when sending the public key
     */
    private void sendPublicRSAKey() throws IOException {
        out.writeObject(publicRSAKey);
        out.flush();
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Object obj = in.readObject();
                    if (obj instanceof EncryptedMessage messageObj) {
                        BigInteger sharedSecret = agreeOnSharedSecret(/* messageObj.getSender() to then get sender public RSA key from CA */);
                        byte[] decryptedMessage = Encryption.decryptAES(messageObj.getEncryptedMessage(), sharedSecret.toByteArray());
                        byte[] decryptedSignature = Encryption.decryptRSA(messageObj.getSignature(), /*senderPublicRSAKey*/);
                        if (!Integrity.verifyDigest(decryptedSignature, Integrity.generateDigest(decryptedMessage))) {
                            throw new RuntimeException("The message has been tampered with.");
                        }
                        System.out.println(new String(decryptedMessage));
                    } else if (obj instanceof Message messageObj) {
                        System.out.println(messageObj.getMessage());
                    }
                } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
                    System.err.println("Ocorreu um erro ao receber a mensagem do servidor: " + e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * Agrees on a shared secret with the client using the Diffie-Hellman key exchange algorithm.
         *
         * @return the shared secret
         * @throws NoSuchAlgorithmException if the algorithm used for the key exchange is not available
         * @throws IOException              if an I/O error occurs when sending the public key
         * @throws ClassNotFoundException   if the class of a serialized object cannot be found
         */
        private BigInteger agreeOnSharedSecret() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
            // Generates the server's private and public keys
            BigInteger privateKey = DiffieHellman.generatePrivateKey();
            BigInteger publicKey = DiffieHellman.generatePublicKey(privateKey);
            // Waits for a client to send its public key
            BigInteger clientPublicKey = (BigInteger) in.readObject();
            // Sends the server's public key to the client
            sendPublicKey(publicKey);
            // Computes the shared secret
            return DiffieHellman.computeSecret(clientPublicKey, privateKey);
        }

        /**
         * Sends the public key to the client.
         *
         * @param publicKey the public key to send
         * @throws IOException if an I/O error occurs when sending the public key
         */
        private void sendPublicKey(BigInteger publicKey) throws IOException {
            out.writeObject(publicKey);
        }

    }

}
