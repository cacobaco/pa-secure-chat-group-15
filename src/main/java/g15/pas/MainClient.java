package g15.pas;

import g15.pas.client.Client;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Config;
import g15.pas.utils.Logger;

import java.util.Scanner;

/**
 * This class contains the main method for starting the Client.
 * It creates an instance of the Client class and starts it.
 */
public class MainClient {

    public static void main(String[] args) {
        String username;

        if (args.length > 0) {
            username = args[0];
        } else {
            username = askForUsername();
        }

        startClient(username);
    }

    /**
     * Asks the user for their username.
     *
     * @return the username entered by the user
     */
    private static String askForUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o seu nome de utilizador: ");
        return scanner.nextLine();
    }

    /**
     * Starts the Client.
     * It creates an instance of the Client class and starts it.
     * If an error occurs during the creation of the Client or during its start,
     * an error message is logged.
     *
     * @param username the username of the client
     */
    private static void startClient(String username) {
        try {
            Client client = new Client(username, Config.SERVER_HOST, Config.SERVER_PORT, Config.CA_HOST, Config.CA_PORT);
            client.start();
        } catch (KeyPairCreationException e) {
            Logger.error("Ocorreu um erro ao criar par de chaves: " + e.getMessage());
        }
    }

}
