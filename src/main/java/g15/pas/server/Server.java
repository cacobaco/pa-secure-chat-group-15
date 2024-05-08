package g15.pas.server;

import g15.pas.exceptions.ConnectionException;
import g15.pas.exceptions.InvalidCertificateException;
import g15.pas.exceptions.InvalidUsernameException;
import g15.pas.message.Message;
import g15.pas.message.messages.BooleanMessage;
import g15.pas.message.messages.CertificateMessage;
import g15.pas.message.messages.TextMessage;
import g15.pas.server.validators.UsernameValidator;
import g15.pas.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private final ServerSocket serverSocket;
    private final ConcurrentHashMap<String, ClientHandler> clients;

    public Server(int port) throws IOException {
        Logger.log("A iniciar servidor...");
        this.serverSocket = new ServerSocket(port);
        this.clients = new ConcurrentHashMap<>();
        Logger.log("Servidor iniciado com sucesso na porta \"%d\".", port);
    }

    public void start() {
        Logger.log("Servidor à espera de conexões...");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Logger.log("Nova conexão aceite.");
                createClientHandler(socket);
            } catch (IOException e) {
                Logger.error("Erro ao aceitar conexão: " + e.getMessage());
                break;
            }
        }

        close();
    }

    public void stop() {
        close();
    }

    private void close() {
        try {
            Logger.log("A fechar servidor...");
            serverSocket.close();
            Logger.log("Servidor fechado com sucesso.");
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao fechar servidor: " + e.getMessage());
        }
    }

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

    private void addClient(String username, ClientHandler clientHandler) {
        clients.put(username, clientHandler);

        for (ClientHandler client : clients.values()) {
            try {
                TextMessage message = new TextMessage("O utilizador \"" + username + "\" ligou-se ao chat.");
                client.sendMessage(message);
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar mensagem de entrada para \"%s\": " + e.getMessage(), client.username);
                client.closeConnection();
            }
        }
    }

    private void removeClient(String username) {
        clients.remove(username);

        for (ClientHandler client : clients.values()) {
            try {
                TextMessage message = new TextMessage("O utilizador \"" + username + "\" desligou-se do chat.");
                client.sendMessage(message);
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar mensagem de saída para \"%s\": " + e.getMessage(), client.username);
                if (!username.equals(client.username)) client.closeConnection();
            }
        }
    }

    private void handleMessage(Message message) {
        Collection<ClientHandler> recipients = clients.values();

        if (message.getRecipients() != null) {
            List<String> messageRecipients = Arrays.asList(message.getRecipients());

            recipients = recipients.stream()
                    .filter(client -> messageRecipients.contains(client.username))
                    .toList();
        }

        for (ClientHandler recipient : recipients) {
            try {
                recipient.sendMessage(message);
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar mensagem para \"%s\": " + e.getMessage(), recipient.username);
                recipient.closeConnection();
            }
        }

        Logger.log("Mensagem de \"%s\" para %d destinatários enviada com sucesso: \"%s\"", message.getSender(), recipients.size(), message.getContent());
    }

    private class ClientHandler extends Thread {

        private final Socket socket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private String username;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                receiveCertificateMessage();
            } catch (InvalidUsernameException | InvalidCertificateException e) {
                Logger.error("Ocorreu um erro ao receber certificado: " + e.getMessage());
                try {
                    sendCertificateResponse(false);
                } catch (ConnectionException ex) {
                    Logger.error("Ocorreu um erro ao enviar resposta do certificado: " + ex.getMessage());
                } finally {
                    closeConnection();
                }
                return;
            }

            try {
                sendCertificateResponse(true);
            } catch (ConnectionException e) {
                Logger.error("Ocorreu um erro ao enviar resposta do certificado: " + e.getMessage());
                closeConnection();
                return;
            }

            addClient(username, this);

            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    handleMessage(message);
                } catch (IOException | ClassNotFoundException e) {
                    Logger.error("Ocorreu um ao ler mensagem do cliente: " + e.getMessage());
                    break;
                }
            }

            closeConnection();
        }

        private void closeConnection() {
            try {
                Logger.log("A fechar conexão...");

                if (username != null) removeClient(username);

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

        private void receiveCertificateMessage() throws InvalidUsernameException, InvalidCertificateException {
            try {
                Logger.log("A receber certificado...");

                CertificateMessage certificateMessage = (CertificateMessage) in.readObject();

                String username = certificateMessage.getSender();

                if (!UsernameValidator.isValid(username)) {
                    throw new InvalidUsernameException("O nome de utilizador não é válido.");
                }

                if (clients.containsKey(username)) {
                    throw new InvalidUsernameException("O nome de utilizador já está em uso.");
                }

                this.username = username;

                Logger.log("Certificado recebido com sucesso.");

                Logger.log("A propagar certificado para os outros clientes...");

                handleMessage(certificateMessage);

                Logger.log("Certificado propagado com sucesso.");
            } catch (IOException | ClassNotFoundException e) {
                throw new InvalidCertificateException(e);
            }
        }

        private void sendCertificateResponse(boolean response) throws ConnectionException {
            Logger.log("A enviar resposta do certificado...");

            BooleanMessage responseMessage = new BooleanMessage(response);
            sendMessage(responseMessage);

            Logger.log("Resposta do certificado enviada com sucesso.");
        }

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
