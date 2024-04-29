package g15.pas.server;

import g15.pas.server.validators.UsernameValidator;
import g15.pas.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private final ServerSocket serverSocket;
    private final ConcurrentHashMap<String, ClientHandler> clients;

    public Server(int port) throws IOException {
        System.out.println("A iniciar servidor...");
        this.serverSocket = new ServerSocket(port);
        this.clients = new ConcurrentHashMap<>();
        System.out.printf("Servidor iniciado na porta %d.\n", port);
    }

    public void start() {
        System.out.println("Servidor pronto para aceitar conexões.");

        while (true) {
            Socket socket = null;

            try {
                System.out.println("À espera de nova conexão...");
                socket = serverSocket.accept();
                System.out.printf("Nova conexão recebida de \"%s:%d.\n", socket.getInetAddress().getHostAddress(), socket.getPort());
            } catch (IOException e) {
                System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                close();
                break;
            }

            try {
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            } catch (IOException e) {
                System.err.println("Erro ao criar handler para cliente: " + e.getMessage());
                close();
                break;
            }
        }
    }

    private void close() {
        try {
            System.out.println("A fechar servidor...");
            serverSocket.close();
            System.out.println("Servidor fechado.");
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket do servidor: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {

        private final Socket socket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private String username;

        public ClientHandler(Socket socket) throws IOException {
            System.out.println("A criar novo handler para cliente...");
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Handler criado com sucesso.");
        }

        @Override
        public void run() {
            if (!handleUsernameMessage()) {
                close();
                return;
            }

            while (true) {
                if (!handleChatMessage()) {
                    break;
                }
            }

            disconnect();
        }

        private boolean handleUsernameMessage() {
            try {
                String username = (String) in.readObject();

                if (!UsernameValidator.isValid(username)) {
                    out.writeBoolean(false);
                    System.out.println("Nome de utilizador inválido: " + username);
                    return false;
                }

                if (clients.containsKey(username)) {
                    out.writeBoolean(false);
                    System.out.println("Nome de utilizador já em uso: " + username);
                    return false;
                }

                out.writeObject(true);
                System.out.println("Novo utilizador: " + username);
                this.username = username;
                clients.put(username, this);

                for (ClientHandler client : clients.values()) {
                    client.sendMessage(String.format("Utilizador \"%s\" entrou no chat.", username));
                }

                return true;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao ler mensagem do cliente: " + e.getMessage());
                return false;
            }
        }

        private boolean handleChatMessage() {
            try {
                String message = (String) in.readObject();

                Message messageObj = new Message(username, message);

                Collection<ClientHandler> recipients = clients.values();

                if (messageObj.getRecipients() != null) {
                    recipients = recipients.stream()
                            .filter(client -> messageObj.getRecipients().contains(client.username))
                            .toList();
                }

                for (ClientHandler recipient : recipients) {
                    recipient.sendMessage(messageObj.getMessage());
                }

                System.out.println("Mensagem de \"" + username + "\" para " + recipients.size() + " destinatários enviada com sucesso: " + messageObj.getMessage());
                return true;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao ler mensagem do cliente: " + e.getMessage());
                return false;
            }
        }

        private void sendMessage(String message) {
            try {
                out.writeObject(message);
            } catch (IOException e) {
                System.err.println("Erro ao enviar mensagem para o cliente: " + e.getMessage());
                disconnect();
            }
        }

        private void close() {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar streams e socket do cliente: " + e.getMessage());
            }
        }

        private void disconnect() {
            clients.remove(username);

            System.out.println("Utilizador desconectado: " + username);

            for (ClientHandler client : clients.values()) {
                client.sendMessage(String.format("Utilizador \"%s\" saiu do chat.", username));
            }

            close();
        }

    }

}
