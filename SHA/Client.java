package SHA;
import java.io.*;
import java.net.*;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1111;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // Input and output streams to communicate with server
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);

            // Read user input (string) to send to the server
            System.out.print("Enter a string to hash: ");
            String inputString = userInput.readLine();

            // Send the string to the server
            serverOutput.println(inputString);

            // Receive the hash from the server and display it
            String serverResponse = serverInput.readLine();
            System.out.println("Hash received from server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

