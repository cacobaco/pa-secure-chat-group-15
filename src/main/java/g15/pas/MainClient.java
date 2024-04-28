package g15.pas;

import g15.pas.client.Client;
import g15.pas.utils.Config;

import java.io.IOException;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        startClient();
    }

    private static String askForUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o seu nome de utilizador: ");
        return scanner.nextLine();
    }

    private static void startClient() {
        String username = askForUsername();

        try {
            Client client = new Client(Config.SERVER_HOST, Config.SERVER_PORT, username);
            client.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o cliente: " + e.getMessage());
        }
    }

}
