package g15.pas.utils;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Certificate {

    private final String username;
    private final PublicKey publicKey;
    private final Map<String, String> additionalInfo;

    public Certificate(String username, PublicKey publicKey) {
        this.username = username;
        this.publicKey = publicKey;
        this.additionalInfo = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void addAdditionalInfo(String key, String value) {
        this.additionalInfo.put(key, value);
    }

}
