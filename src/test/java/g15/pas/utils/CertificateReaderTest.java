package g15.pas.utils;

import g15.pas.utils.Certificate;
import g15.pas.utils.CertificateReader;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

public class CertificateReaderTest {

    private String convertToPEM(Certificate certificate) {
        String encodedUsername = Base64.getEncoder().encodeToString(certificate.getUsername().getBytes());
        String encodedKey = Base64.getEncoder().encodeToString(certificate.getPublicKey().getEncoded());
        String encodedSignature = Base64.getEncoder().encodeToString(certificate.getSignature());

        StringBuilder pemContent = new StringBuilder();
        pemContent.append("-----BEGIN CERTIFICATE-----\n");
        pemContent.append(encodedUsername).append("\n");
        pemContent.append(encodedKey).append("\n");
        pemContent.append(encodedSignature).append("\n");
        pemContent.append("-----END CERTIFICATE-----\n");

        return pemContent.toString();
    }

    private void writeToFile(String content, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Falha ao excluir o arquivo temporário: " + e.getMessage());
        }
    }
}
