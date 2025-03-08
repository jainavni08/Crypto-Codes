import java.io.*;
import java.net.*;

public class Server {

    // Define constants (same as before)
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for clients...");

            // Accept incoming client connections
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected.");

                // Input and output streams to communicate with client
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the input message from the client
                String inputMessage = input.readLine();
                System.out.println("Received from client: " + inputMessage);

                // Compute the hash using the custom hash function
                byte[] hash = CustomHash.customHash(inputMessage.getBytes());

                // Send the hash back to the client as a hex string
                output.println(bytesToHex(hash));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convert byte array to hex string (for easy display)
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}

