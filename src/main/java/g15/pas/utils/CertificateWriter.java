package g15.pas.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Base64;

/**
 * This class provides utility methods for writing a Certificate to a file.
 */
public class CertificateWriter {

    /**
     * Writes a Certificate to a file.
     *
     * @param certificate the Certificate to be written
     * @param filePath    the path to the file
     * @throws Exception if an error occurs during writing
     */
    public static void writeCertificate(Certificate certificate, String filePath) throws Exception {
        String pemContent = convertToPEM(certificate);

        Files.createDirectories(Paths.get(filePath).getParent());
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(pemContent);
        writer.close();
    }

    /**
     * Converts a Certificate to a PEM content string.
     *
     * @param certificate the Certificate to be converted
     * @return the PEM content string
     */
    static String convertToPEM(Certificate certificate) {
        String serialNumber = certificate.getSerialNumber();
        PublicKey publicKey = certificate.getPublicKey();
        String username = certificate.getUsername();
        Long expirationDate = certificate.getExpirationDate();
        byte[] signature = certificate.getSignature();

        byte[] serialNumberBytes = serialNumber.getBytes();
        String encodedSerialNumber = Base64.getEncoder().encodeToString(serialNumberBytes);

        byte[] keyBytes = publicKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);

        byte[] usernameBytes = username.getBytes();
        String encodedUsername = Base64.getEncoder().encodeToString(usernameBytes);

        StringBuilder pemContent = new StringBuilder();
        pemContent.append("-----BEGIN CERTIFICATE-----\n");
        pemContent.append(encodedSerialNumber).append("\n");
        pemContent.append(encodedUsername).append("\n");
        pemContent.append(encodedKey).append("\n");

        if (expirationDate != null) {
            byte[] expirationDateBytes = expirationDate.toString().getBytes();
            String encodedExpirationDate = Base64.getEncoder().encodeToString(expirationDateBytes);
            pemContent.append(encodedExpirationDate).append("\n");
        }

        if (signature != null) {
            String encodedSignature = Base64.getEncoder().encodeToString(signature);
            pemContent.append(encodedSignature).append("\n");
        }

        pemContent.append("-----END CERTIFICATE-----\n");
        return pemContent.toString();
    }

}
