package g15.pas.utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class PublicKeyRecipients implements Serializable {
    private final BigInteger publicKey;
    private final List<String> recipients;
    public PublicKeyRecipients(BigInteger publicKey, List<String> recipients) {
        this.publicKey = publicKey;
        this.recipients = recipients;
    }
    public BigInteger getPublicKey() {
        return publicKey;
    }
    public List<String> getRecipients() {
        return recipients;
    }
}
