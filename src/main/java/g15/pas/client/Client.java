package g15.pas.client;

import g15.pas.client.exceptions.KeyPairCreationException;
import g15.pas.utils.Certificate;
import g15.pas.utils.Encryption;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
    private Thread caListener;

    private final Socket socketCA;
    private final ObjectOutputStream outCA;
    //private final ObjectInputStream inCA;

    public Client(String serverHost, int serverPort,String caHost, int caPort, String username) throws KeyPairCreationException, IOException {

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
        this.username = username;
        System.out.println("Conectado ao servidor.");

        this.socketCA = new Socket(caHost, caPort);
        this.outCA = new ObjectOutputStream(socketCA.getOutputStream());
        System.out.println("teste2");
        //this.inCA = new ObjectInputStream(socketCA.getInputStream());

        System.out.println("Conectado ao ca.");

        // Sends the certificate
        System.out.println("Guardando certificado...");
        saveCertificateToFolder(certificate, "CertificateStorage");
        System.out.println("Certificate saved to folder successfully.");


    }

    public void start() {


        System.out.println("A enviar nome de utilizador...");
        if (!sendUsernameMessage()) {
            close();
            return;
        }


        serverListener = new Thread(new ServerListener());
        serverListener.start();


        //caListener = new Thread(new caListener());
        //caListener.start();

        messageCA();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();

            try {
                out.writeObject(message);
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
            caListener.interrupt();
            in.close();
            out.close();
            socket.close();
            //inCA.close();
            outCA.close();
            socketCA.close();
            System.out.println("Cliente fechado.");
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao fechar as streams e o socket: " + e.getMessage());
        }
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

    private void saveCertificateToFolder(Certificate certificate, String folderPath) {

        try {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // Create the folder if it doesn't exist
            }

            String fileName = "certificate" + username + ".txt";
            File certificateFile = new File(folder, fileName);

            StringBuilder certificateContent = new StringBuilder();
            certificateContent.append("Username: ").append(username).append("\n");
            certificateContent.append("Public Key: ").append(publicKey).append("\n");


            try (FileWriter writer = new FileWriter(certificateFile)) {
                writer.write(certificateContent.toString());
            }


            System.out.println("Certificado salvo: " + certificateFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving certificate to folder: " + e.getMessage());
        }
    }

    private void messageCA() {
        String messageCA="teste";
        try {
            outCA.writeObject(messageCA); // Envia a mensagem para o CertificateAuthority
            outCA.flush(); // Limpa o buffer de saída
            System.out.println("Mensagem enviada para o CertificateAuthority: " + messageCA);
        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem para o CertificateAuthority: " + e.getMessage());
        }
    }




    private class ServerListener implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String message = (String) in.readObject();
                    System.out.println(message);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Ocorreu um erro ao receber a mensagem do servidor: " + e.getMessage());
                }
            }
        }

    }

    /*private class caListener implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String messageCA= (String) inCA.readObject();
                    System.out.println(messageCA);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Ocorreu um erro ao receber a mensagem do ca: " + e.getMessage());
                }
            }
        }

    }*/

    }
