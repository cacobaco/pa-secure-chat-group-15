package g15.pas.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Base64;

public class CertificateWriter {

    public static void writeCertificate(Certificate certificate, String filePath) throws Exception {
        String pemContent = convertToPEM(certificate);

        Files.createDirectories(Paths.get(filePath).getParent());
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(pemContent);
        writer.close();
    }

    private static String convertToPEM(Certificate certificate) {
        String serialNumber = certificate.getSerialNumber();
        PublicKey publicKey = certificate.getPublicKey();
        String username = certificate.getUsername();
        byte[] signature = certificate.getSignature();

        byte[] serialNumberBytes = serialNumber.getBytes();
        String encodedSerialNumber = Base64.getEncoder().encodeToString(serialNumberBytes);

        byte[] keyBytes = publicKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);

        byte[] usernameBytes = username.getBytes();
        String encodedUsername = Base64.getEncoder().encodeToString(usernameBytes);

        if (signature == null) {
            return "-----BEGIN CERTIFICATE-----\n" +
                    encodedSerialNumber + "\n" +
                    encodedUsername + "\n" +
                    encodedKey + "\n" +
                    "-----END CERTIFICATE-----";
        }

        String encodedSignature = Base64.getEncoder().encodeToString(signature);

        return "-----BEGIN CERTIFICATE-----\n" +
                encodedSerialNumber + "\n" +
                encodedUsername + "\n" +
                encodedKey + "\n" +
                encodedSignature + "\n" +
                "-----END CERTIFICATE-----";
    }

}
