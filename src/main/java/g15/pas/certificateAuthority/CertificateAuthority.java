package g15.pas.certificateAuthority;

import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Encryption;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CertificateAuthority {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final ServerSocket caSocket;
    ;

    public CertificateAuthority(int portCA) throws KeyPairCreationException, IOException   {

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
                    if (receivedObject instanceof String) {
                        String message = (String) receivedObject;
                        processMessage(message);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Erro ao ler mensagem do cliente: " + e.getMessage());
                } finally {
                    if (socket != null) {
                        System.out.println("conecção ded.");
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
    private void processMessage(String message) {
        System.out.println("Mensagem recebida: " + message);
        // Aqui você pode adicionar a lógica para processar a mensagem recebida

        String filePath  = "CertificateStorage\\certificatebruno.txt";
        String textToAdd = "This is some text to add to the file.";

        try {
            // Open the file in append mode
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the text to the file
            bufferedWriter.write(textToAdd);
            bufferedWriter.newLine(); // Add a new line

            // Close the file
            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Text added to the file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }


}


