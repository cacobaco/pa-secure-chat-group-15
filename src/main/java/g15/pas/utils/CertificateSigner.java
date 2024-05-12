package g15.pas.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.util.Date;

public class CertificateSigner {

    public static Certificate signCertificate(Certificate certificate, PrivateKey privateKey) throws Exception {
        long expirationDate = (new Date()).getTime() + (Config.CA_CERTIFICATE_VALIDITY * 60 * 1000);
        certificate.setExpirationDate(expirationDate);

        byte[] certificateBytes = serializeCertificate(certificate);
        byte[] hash = hashCertificate(certificateBytes);

        Signature signatureInstance = Signature.getInstance("SHA256withRSA");
        signatureInstance.initSign(privateKey);
        signatureInstance.update(hash);
        byte[] signature = signatureInstance.sign();

        certificate.setSignature(signature);

        return certificate;
    }

    public static boolean verifyCertificate(Certificate certificate, PublicKey publicKey) throws Exception {
        byte[] certificateBytes = serializeCertificate(certificate);
        byte[] hash = hashCertificate(certificateBytes);

        Signature signatureInstance = Signature.getInstance("SHA256withRSA");
        signatureInstance.initVerify(publicKey);
        signatureInstance.update(hash);

        return signatureInstance.verify(certificate.getSignature());
    }

    private static byte[] serializeCertificate(Certificate certificate) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(certificate.getSerialNumber());
        objectOutputStream.writeObject(certificate.getUsername());
        objectOutputStream.writeObject(certificate.getPublicKey());
        objectOutputStream.writeObject(certificate.getExpirationDate());

        objectOutputStream.flush();
        objectOutputStream.close();

        byte[] certificateBytes = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream.close();

        return certificateBytes;
    }

    private static byte[] hashCertificate(byte[] certificateBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(certificateBytes);
    }

}
