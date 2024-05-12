package g15.pas;

import g15.pas.ca.CertificateAuthority;
import g15.pas.exceptions.KeyPairCreationException;
import g15.pas.utils.Config;
import g15.pas.utils.Logger;

import java.io.IOException;

/**
 * This class contains the main method for starting the Certificate Authority (CA).
 * It creates an instance of the CertificateAuthority class and starts it.
 */
public class MainCA {

    public static void main(String[] args) {
        startCA();
    }

    /**
     * Starts the Certificate Authority (CA).
     * It creates an instance of the CertificateAuthority class and starts it.
     * If an error occurs during the creation of the CertificateAuthority or during its start,
     * an error message is logged.
     */
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
