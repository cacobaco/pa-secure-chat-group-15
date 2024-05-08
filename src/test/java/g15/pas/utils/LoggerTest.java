package g15.pas.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerTest {

    @Test
    void shouldLogMessage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Logger.log("Hello, %s!", "World");
        assertEquals("Hello, World!\n", outContent.toString());
    }

    @Test
    void shouldLogErrorMessage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Logger.error("Error: %s", "Something went wrong");
        assertEquals("[ERRO] Error: Something went wrong\n", outContent.toString());
    }

}
