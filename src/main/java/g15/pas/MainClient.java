package g15.pas;

import g15.pas.client.Client;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Config;
import g15.pas.utils.Logger;

import java.util.Scanner;

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

    private static String askForUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o seu nome de utilizador: ");
        return scanner.nextLine();
    }

    private static void startClient(String username) {
        try {
            Client client = new Client(username, Config.SERVER_HOST, Config.SERVER_PORT);
            client.start();
        } catch (KeyPairCreationException e) {
            Logger.error("Ocorreu um erro ao criar par de chaves: " + e.getMessage());
        }
    }

}
