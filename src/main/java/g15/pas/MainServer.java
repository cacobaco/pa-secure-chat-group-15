package g15.pas;

import g15.pas.server.Server;
import g15.pas.utils.Config;
import g15.pas.utils.Logger;

import java.io.IOException;

/**
 * This class contains the main method for starting the Server.
 * It creates an instance of the Server class and starts it.
 */
public class MainServer {

    public static void main(String[] args) {
        startServer();
    }

    /**
     * Starts the Server.
     * It creates an instance of the Server class and starts it.
     * If an error occurs during the creation of the Server or during its start,
     * an error message is logged.
     */
    private static void startServer() {
        try {
            Server server = new Server(Config.SERVER_PORT);
            server.start();
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao criar o servidor: " + e.getMessage());
        }
    }

}
