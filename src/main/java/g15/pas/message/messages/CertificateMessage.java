package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.utils.Certificate;

/**
 * This class represents a certificate message that extends the Message class.
 * The message content is of type Certificate.
 * It provides constructors to create a CertificateMessage with content, sender, and recipients.
 */
public class CertificateMessage extends Message<Certificate> {

    /**
     * Constructs a new CertificateMessage with the specified content.
     * The sender and recipients are set to null.
     *
     * @param certificate the content of the message
     */
    public CertificateMessage(Certificate certificate) {
        super(certificate);
    }

    /**
     * Constructs a new CertificateMessage with the specified content and sender.
     * The recipients are set to null.
     *
     * @param certificate the content of the message
     * @param sender      the sender of the message
     */
    public CertificateMessage(Certificate certificate, String sender) {
        super(certificate, sender);
    }

    /**
     * Constructs a new CertificateMessage with the specified content, sender, and recipients.
     *
     * @param certificate the content of the message
     * @param sender      the sender of the message
     * @param recipients  the recipients of the message
     */
    public CertificateMessage(Certificate certificate, String sender, String[] recipients) {
        super(certificate, sender, recipients);
    }

}
