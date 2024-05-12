package g15.pas.client;

import g15.pas.exceptions.ConnectionException;
import g15.pas.exceptions.KeyPairCreationException;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ClientTest {

    @Test
    public void testClientStartAndStop() {
        // Set up the necessary parameters for the Client constructor
        String username = "testUser";
        String serverHost = "localhost";
        int serverPort = 1234;
        String caHost = "ca.example.com";
        int caPort = 5678;

        // Create a new Client instance
        Client client = null;
        try {
            client = new Client(username, serverHost, serverPort, caHost, caPort);
        } catch (KeyPairCreationException e) {
            fail("Failed to create client: " + e.getMessage());
        }

        // Start the client
        client.start();

        // Perform assertions to check if the client is in the desired state after starting
        // For example, you can check if the client is connected to the server

        // Stop the client
        client.stop();

        // Perform assertions to check if the client is in the desired state after stopping
        // For example, you can check if the client is disconnected from the server
    }

    public void setUp() {
        // Set up the necessary parameters for the Client constructor
        String username = "testUser";
        String serverHost = "localhost";
        int serverPort = 1234;
        String caHost = "ca.example.com";
        int caPort = 5678;

        // Create a new Client instance
        try {
            Client client = new Client(username, serverHost, serverPort, caHost, caPort);
        } catch (KeyPairCreationException e) {
            fail("Failed to create client: " + e.getMessage());
        }
    }

    @Test
    public void testClientSendTextMessage() {
        Client client;
        try {
            client = new Client("username", "serverHost", 1234, "caHost", 5678);
        } catch (KeyPairCreationException e) {
            fail("Failed to create client: " + e.getMessage());
            return; // Ensure the test stops execution if client creation fails
        }

        // Start the client
        client.start();

        // Send a text message
        String message = "Hello, world!";
        try {
            client.sendMessage(message);
        } catch (ConnectionException e) {
            fail("Failed to send message: " + e.getMessage());
        }

        // Perform assertions to check if the message was sent successfully
        // For example, you could check if the message was received by the server

        // Stop the client
        client.stop();
    }


}
