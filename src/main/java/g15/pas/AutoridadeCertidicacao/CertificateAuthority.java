package g15.pas.AutoridadeCertidicacao;

import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Encryption;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.*;


public class CertificateAuthority {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final ServerSocket caSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String username;
    private String filePath;


    public CertificateAuthority(int portCA) throws KeyPairCreationException, IOException {



        System.out.println("A iniciar servidor...");
        this.caSocket = new ServerSocket(portCA);

        System.out.printf("Servidor iniciado na porta %d.\n", portCA);


        // Create key pair
        //System.out.println("A gerar par de chaves...");
        KeyPair keyPair;
        try {
            keyPair = Encryption.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyPairCreationException("Erro ao criar o par de chaves: " + e.getMessage(), e);
        }
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        // System.out.println("Par de chaves gerado com sucesso.");


    }

    public void start() throws IOException {
        System.out.println("Servidor pronto para aceitar conexões.");
        while (true) {
            Socket socket = null;
            while (true) {
                try {

                    socket = caSocket.accept();
                    ObjectInputStream inCA = new ObjectInputStream(socket.getInputStream());
                    Object receivedObject = inCA.readObject();

                    //sha-256 hash
                    if (receivedObject instanceof String) {
                        username = (String) receivedObject;
                        String filePath = "CertificateStorage\\certificate"+ username + ".txt";
                        System.out.println("Mensagem recebida do cliente: " + username);
                        calculateSHA256(Paths.get(filePath));
                        System.out.println("SHA-256 hash: " + filePath);
                        //digital signature
                        try {
                            byte[] data = Files.readAllBytes(Paths.get(filePath));
                            byte[] digitalSignature = generateDigitalSignature(privateKey, data);
                            // Append the digital signature to the file
                            try (FileWriter writer = new FileWriter(filePath, true)) {
                                writer.write("\nDigital Signature: " + bytesToHex(digitalSignature));


                            } catch (IOException e) {
                                throw new RuntimeException("Erro escrevendo assinatura digital", e);
                            }


                            System.out.println("Digital signature generated and appended to the file.");
                        } catch (IOException e) {
                            System.err.println("Error reading file for digital signature: " + e.getMessage());
                        }

                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Erro ao ler mensagem do cliente: " + e.getMessage());
                } finally {
                    if (socket != null) {
                        sendMessage();
                        saveSignedCertificate(filePath);
                        System.out.println("coneccao ded.");
                        socket.close(); // Close the socket after processing the message
                    }
                }
            }

        }
    }

    /* private void close() {
         try {
             System.out.println("A fechar servidor...");
             caSocket.close();
             System.out.println("Servidor fechado.");
         } catch (IOException e) {
             System.err.println("Erro ao fechar socket do servidor: " + e.getMessage());
         }
     }
 }*/

    private static void calculateSHA256(Path filePath) {
        try (InputStream is = new FileInputStream(filePath.toFile())){
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;

            try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            byte[] hashBytes = digest.digest();
            String hashValue = bytesToHex(hashBytes);

            // Append the hash value to the file
            try (FileWriter writer = new FileWriter(filePath.toFile(), true)) {
                writer.write("\nSHA-256 Hash: " + hashValue);
            } catch (IOException e) {
                throw new RuntimeException("Error writing hash value to the file", e);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating SHA-256 hash", e);
        }

    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] generateDigitalSignature(PrivateKey privateKey, byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("Error generating digital signature: " + e.getMessage(), e);
        }
    }


    private void saveSignedCertificate(String folderPath) {
        String sourceFilePath = "CertificateStorage\\certificate" + username + ".txt";
        String destinationFilePath = "CertificateStorageSigned\\certificate" + username + ".txt";

        Path sourcePath = Paths.get(sourceFilePath);
        if (!Files.exists(sourcePath)) {
            System.err.println("Source file does not exist: " + sourceFilePath);
            return;
        }

        try {
            Path destinationPath = Paths.get(destinationFilePath);
            if (!Files.exists(destinationPath)) {
                Files.createDirectories(destinationPath.getParent());
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Signed certificate saved to: " + destinationFilePath);
            } else {
                System.err.println("Destination file already exists: " + destinationFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving signed certificate: " + e.getMessage(), e);
        }
    }


    public void sendMessage(){



    }


}






