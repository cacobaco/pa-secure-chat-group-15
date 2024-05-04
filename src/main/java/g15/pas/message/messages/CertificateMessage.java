package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.utils.Certificate;

/**
 * This class extends the Message class with Certificate as the type parameter.
 * It represents a certificate message that can be sent from a sender to multiple recipients.
 * The content of a CertificateMessage is a Certificate, which represents the certificate to be sent.
 */
public class CertificateMessage extends Message<Certificate> {

    /**
     * Constructor for CertificateMessage with only content.
     *
     * @param certificate The content of the message
     */
    public CertificateMessage(Certificate certificate) {
        super(certificate);
    }

    /**
     * Constructor for CertificateMessage with content and sender.
     *
     * @param certificate The content of the message
     * @param sender      The sender of the message
     */
    public CertificateMessage(Certificate certificate, String sender) {
        super(certificate, sender);
    }

    /**
     * Constructor for CertificateMessage with content, sender, and recipients.
     *
     * @param certificate The content of the message
     * @param sender      The sender of the message
     * @param recipients  The recipients of the message
     */
    public CertificateMessage(Certificate certificate, String sender, String[] recipients) {
        super(certificate, sender, recipients);
    }

}
