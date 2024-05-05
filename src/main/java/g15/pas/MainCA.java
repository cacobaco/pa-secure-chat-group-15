package g15.pas;

import g15.pas.CA.CertificateAuthority;
import g15.pas.client.exceptions.KeyPairCreationException;
import g15.pas.utils.Config;

import java.io.IOException;

public class MainCA {

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        try {
            CertificateAuthority ca = new CertificateAuthority(Config.CA_PORT);
            ca.start();
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao criar o servidor: " + e.getMessage());
        } catch (KeyPairCreationException e) {
            throw new RuntimeException(e);
        }
    }
}
