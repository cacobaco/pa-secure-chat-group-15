package g15.pas.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.util.Date;

/**
 * This class provides utility methods for signing and verifying Certificates.
 */
public class CertificateSigner {

    /**
     * Signs a Certificate by setting its expiration date and signature.
     *
     * @param certificate the Certificate to be signed
     * @param privateKey  the private key to be used for signing
     * @return the signed Certificate
     * @throws Exception if an error occurs during signing
     */
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

    /**
     * Verifies a Certificate by checking its signature.
     *
     * @param certificate the Certificate to be verified
     * @param publicKey   the public key to be used for verification
     * @return true if the Certificate is verified, false otherwise
     * @throws Exception if an error occurs during verification
     */
    public static boolean verifyCertificate(Certificate certificate, PublicKey publicKey) throws Exception {
        byte[] certificateBytes = serializeCertificate(certificate);
        byte[] hash = hashCertificate(certificateBytes);

        Signature signatureInstance = Signature.getInstance("SHA256withRSA");
        signatureInstance.initVerify(publicKey);
        signatureInstance.update(hash);

        return signatureInstance.verify(certificate.getSignature());
    }

    /**
     * Serializes a Certificate into a byte array.
     *
     * @param certificate the Certificate to be serialized
     * @return the byte array representation of the Certificate
     * @throws IOException if an error occurs during serialization
     */
    static byte[] serializeCertificate(Certificate certificate) throws IOException {
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

    /**
     * Hashes a byte array representation of a Certificate.
     *
     * @param certificateBytes the byte array representation of the Certificate
     * @return the hash of the Certificate
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     */
    static byte[] hashCertificate(byte[] certificateBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(certificateBytes);
    }

}
