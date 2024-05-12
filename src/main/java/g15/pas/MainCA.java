package g15.pas;

import g15.pas.ca.CertificateAuthority;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Config;
import g15.pas.utils.Logger;

import java.io.IOException;

public class MainCA {

    public static void main(String[] args) {
        startCA();
    }

    private static void startCA() {
        try {
            CertificateAuthority ca = new CertificateAuthority(Config.CA_PORT) {
                @Override
                public void connect() {

                }
            };
            ca.start();
        } catch (IOException | KeyPairCreationException e) {
            Logger.error("Ocorreu um erro ao criar a CA: " + e.getMessage());
        }
    }

}
