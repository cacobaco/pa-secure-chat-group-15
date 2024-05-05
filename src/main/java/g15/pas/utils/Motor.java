package g15.pas.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Motor {

    private final char[] alphabet = { 'A' , 'B' , 'C' , 'D' , 'E' , 'F' , 'G' , 'H' , 'I' , 'J' , 'K' , 'L' , 'M' , 'N' , 'O' , 'P' , 'Q' , 'R' , 'S' , 'T' , 'U' , 'V' , 'W' , 'X' , 'Y' , 'Z' , 'a' , 'b' , 'c' , 'd' , 'e' , 'f' , 'g' , 'h' , 'i' , 'j' , 'k' , 'l' , 'm' , 'n' , 'o' , 'p' , 'q' , 'r' , 's' , 't' , 'u' , 'v' , 'w' , 'x' , 'y' , 'z' , ' ' };
    private final List < Character > code;

    public Motor ( int initialRotation ) {
        code = new ArrayList <> ( alphabet.length );
        initialiseCode ( );
        rotate ( initialRotation );

    }

    private void initialiseCode ( ) {
        for ( char c : alphabet ) {
            code.add ( c );
        }
    }

    public void rotate ( int distance ) {
        Collections.rotate ( code , distance );
    }

    private int getCharPosition ( char c ) {
        return switch ( c ) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            case 'I' -> 8;
            case 'J' -> 9;
            case 'K' -> 10;
            case 'L' -> 11;
            case 'M' -> 12;
            case 'N' -> 13;
            case 'O' -> 14;
            case 'P' -> 15;
            case 'Q' -> 16;
            case 'R' -> 17;
            case 'S' -> 18;
            case 'T' -> 19;
            case 'U' -> 20;
            case 'V' -> 21;
            case 'W' -> 22;
            case 'X' -> 23;
            case 'Y' -> 24;
            case 'Z' -> 25;
            case 'a' -> 26;
            case 'b' -> 27;
            case 'c' -> 28;
            case 'd' -> 29;
            case 'e' -> 30;
            case 'f' -> 31;
            case 'g' -> 32;
            case 'h' -> 33;
            case 'i' -> 34;
            case 'j' -> 35;
            case 'k' -> 36;
            case 'l' -> 37;
            case 'm' -> 38;
            case 'n' -> 39;
            case 'o' -> 40;
            case 'p' -> 41;
            case 'q' -> 42;
            case 'r' -> 43;
            case 's' -> 44;
            case 't' -> 45;
            case 'u' -> 46;
            case 'v' -> 47;
            case 'w' -> 48;
            case 'x' -> 49;
            case 'y' -> 50;
            case 'z' -> 51;
            case ' ' -> 52;
            default -> throw new IllegalArgumentException ( c + " is not a valid character" );
        };
    }

    public char encrypt ( char c ) {
        return code.get ( getCharPosition ( c ) );
    }

    public char decrypt ( char c ) {
        return alphabet[ code.indexOf ( c ) ];
    }

}