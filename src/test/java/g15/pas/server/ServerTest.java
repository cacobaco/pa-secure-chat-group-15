package g15.pas.server;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


import java.io.*;
import java.net.*;

import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class ServerTest {


    private Server server;
    private int port = 8080;
    private ServerSocket serverSocket;

    @BeforeEach
    public void setUp() {
        try {
            server = new Server(port);
            new Thread(() -> {
                server.start();
            }).start();

            // Wait for the server to start (optional)
            try {
                Thread.sleep(100); // Adjust the delay time as needed
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while waiting for server to start", e);
            }
        } catch (IOException e) {
            // Print stack trace for debugging
            e.printStackTrace();
            // Set server to null to indicate failure
            server = null;
        }
    }

    @AfterEach
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
        try {
            if (serverSocket != null) {
                serverSocket.close(); // Close serverSocket if it's not null
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClientConnection() throws IOException, InterruptedException {
        // Simulate client connection
        Socket clientSocket = new Socket("localhost", port);

        assertTrue(clientSocket.isConnected());

        // Close the client socket
        clientSocket.close();
    }


    @Test
    public void testServerStartFailure() {
        // Try to start server on an already used port
        assertThrows(IOException.class, () -> new Server(port).start());
    }}


