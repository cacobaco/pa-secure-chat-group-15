package g15.pas.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.PublicKey;
import java.util.Base64;

public class CertificateReader {

    public static Certificate readCertificate(String filePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        StringBuilder pemContent = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            pemContent.append(line).append("\n");
        }

        reader.close();

        return convertFromPEM(pemContent.toString());
    }

    private static Certificate convertFromPEM(String pemContent) throws Exception {
        String[] lines = pemContent.split("\n");

        String encodedUsername = lines[1];
        String encodedKey = lines[2];

        byte[] usernameBytes = Base64.getDecoder().decode(encodedUsername);
        String username = new String(usernameBytes);

        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        PublicKey publicKey = Encryption.convertBytesToPublicKey(keyBytes);

        Certificate certificate = new Certificate(username, publicKey);

        if (lines.length == 4) {
            return certificate;
        }

        String encodedSignature = lines[3];
        byte[] signature = Base64.getDecoder().decode(encodedSignature);

        certificate.setSignature(signature);

        return certificate;
    }

}
