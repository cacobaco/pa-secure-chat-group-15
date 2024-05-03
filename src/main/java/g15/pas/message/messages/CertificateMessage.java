package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.utils.Certificate;

public class CertificateMessage extends Message<Certificate> {

    public CertificateMessage(Certificate certificate) {
        super(certificate);
    }

    public CertificateMessage(Certificate certificate, String sender) {
        super(certificate, sender);
    }

    public CertificateMessage(Certificate certificate, String sender, String[] recipients) {
        super(certificate, sender, recipients);
    }

}
