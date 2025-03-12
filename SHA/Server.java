package SHA;
import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 1111;

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
                output.println(CustomHash.bytesToHex(hash));  // Use bytesToHex from CustomHash
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
