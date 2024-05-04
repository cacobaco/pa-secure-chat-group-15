package g15.pas;

import g15.pas.certificateAuthority.CertificateAuthority;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Config;

import java.io.IOException;

public class MainCA {

    public static void main(String[] args) throws IOException {
        startServer();
    }

    private static void startServer() throws IOException {
        try {
            CertificateAuthority ca = new CertificateAuthority(Config.CA_PORT);
            ca.start();
        } catch (KeyPairCreationException e) {
            throw new RuntimeException(e);
        }
    }
}
