package g15.pas.utils;

public class CertificateRevoker {

    public static void revokeCertificate(Certificate certificate) throws Exception {
        CertificateWriter.writeCertificate(certificate, Config.CA_CRL_PATH + "/" + certificate.getUsername() + ".pem");
    }

    public static boolean isRevoked(Certificate certificate) {
        try {
            Certificate revoked = CertificateReader.readCertificate(Config.CA_CRL_PATH + "/" + certificate.getUsername() + ".pem");

            return revoked.getSerialNumber().equals(certificate.getSerialNumber());
        } catch (Exception e) {
            return false;
        }
    }

}
