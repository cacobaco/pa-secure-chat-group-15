package g15.pas.message.enums;

/**
 * This enum represents the types of commands that can be sent in a message.
 * Each command type corresponds to a specific action that the recipient of the message should perform.
 */
public enum CommandType {
    REQUEST_CERTIFICATE,
    REQUEST_CERTIFICATE_SIGN,
    REQUEST_PUBLIC_KEY,
    REQUEST_PUBLIC_DH_KEY,
}
