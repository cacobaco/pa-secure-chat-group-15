package g15.pas.utils;

import java.util.Date;

/**
 * This class provides utility methods for revoking and checking the status of Certificates.
 */
public class CertificateRevoker {

    /**
     * Revokes a Certificate by writing it to the Certificate Revocation List (CRL).
     *
     * @param certificate the Certificate to be revoked
     * @throws Exception if an error occurs during writing
     */
    public static void revokeCertificate(Certificate certificate) throws Exception {
        CertificateWriter.writeCertificate(certificate, Config.CA_CRL_PATH + "/" + certificate.getUsername() + ".pem");
    }

    /**
     * Checks if a Certificate is revoked by comparing its serial number with the one in the CRL.
     *
     * @param certificate the Certificate to be checked
     * @return true if the Certificate is revoked, false otherwise
     */
    public static boolean isRevoked(Certificate certificate) {
        try {
            Certificate revoked = CertificateReader.readCertificate(Config.CA_CRL_PATH + "/" + certificate.getUsername() + ".pem");

            return revoked.getSerialNumber().equals(certificate.getSerialNumber());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a Certificate is expired by comparing its expiration date with the current time.
     *
     * @param certificate the Certificate to be checked
     * @return true if the Certificate is expired, false otherwise
     */
    public static boolean isExpired(Certificate certificate) {
        return certificate.getExpirationDate() < (new Date()).getTime();
    }

}
