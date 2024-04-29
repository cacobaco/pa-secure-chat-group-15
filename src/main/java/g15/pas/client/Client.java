package g15.pas.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final String username;
    private Thread serverListener;

    public Client(String serverHost, int serverPort, String username) throws IOException {
        this.socket = new Socket(serverHost, serverPort);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.username = username;
        System.out.println("Conectado ao servidor.");
    }

    public void start() {
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
            in.close();
            out.close();
            socket.close();
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

}
