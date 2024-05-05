package g15.pas.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PemFile {
    private final Motor motor1;
    private final Motor motor2;
    private final Motor motor3;

    public PemFile ( int initialRotationMotor1 , int initialRotationMotor2 , int initialRotationMotor3 ) {
        motor1 = new Motor ( initialRotationMotor1 );
        motor2 = new Motor ( initialRotationMotor2 );
        motor3 = new Motor ( initialRotationMotor3 );
    }

    /**
     * Converts a char to its ASCII value.
     *
     * @param c the char to convert
     *
     * @return the ASCII value of the char
     */
    private static int getASCIFromChar ( char c ) {
        return c;
    }

    /**
     * Encrypts, using the ASCII-encryption method, a given file and writes the encrypted content to another file
     * specified by the user.
     *
     * @param originFile      the path of the file to encrypt
     * @param destinationFile the path of the file to write the encrypted content
     */
    public void encryptFile ( String originFile , String destinationFile ) {
        File original = new File ( originFile );
        try {
            Scanner reader = new Scanner ( original );
            StringBuilder encryptedContent = new StringBuilder ( );
            while ( reader.hasNextLine ( ) ) {
                String input = reader.nextLine ( );
                if ( encryptedContent.isEmpty ( ) ) {
                    encryptedContent.append ( encrypt ( input ) );
                } else {
                    encryptedContent.append ( "\n" ).append ( encrypt ( input ) );
                }
            }
            FileWriter encryptedFile = new FileWriter ( destinationFile );
            encryptedFile.write ( encryptedContent.toString ( ) );
            encryptedFile.close ( );
        } catch ( IOException e ) {
            e.printStackTrace ( );
        }

    }

    /**
     * Encrypts a given block of text using ASCII-encryption combined with enigma cipher.
     *
     * @param block the block of text to encrypt
     *
     * @return the encrypted block of text
     */
    private String encrypt ( String block ) {
        StringBuilder encryptedBlock = new StringBuilder ( );
        for ( int i = 0 ; i < block.length ( ) ; i++ ) {
            char resultMotor1 = motor1.encrypt ( block.charAt ( i ) );
            char resultMotor2 = motor2.encrypt ( resultMotor1 );
            char resultMotor3 = motor3.encrypt ( resultMotor2 );
            motor1.rotate ( 1 );
            if ( i % 2 == 0 ) {
                motor2.rotate ( 1 );
            }
            if ( i % 3 == 0 ) {
                motor3.rotate ( 1 );
            }
            encryptedBlock.append ( getASCIFromChar ( resultMotor3 ) ).append ( '-' );
        }
        return encryptedBlock.toString ( );
    }


}