package g15.pas;

import g15.pas.server.Server;
import g15.pas.utils.Config;

import java.io.IOException;

public class MainServer {

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        try {
            Server server = new Server(Config.SERVER_PORT);
            server.start();
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao criar o servidor: " + e.getMessage());
        }
    }

}
