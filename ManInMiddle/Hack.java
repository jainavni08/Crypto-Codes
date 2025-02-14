package ManInMiddle;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Hack {

    // Fast modular exponentiation function
    public static int modExp(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) // If exponent is odd
                result = (result * base) % mod;
            exp = exp >> 1; // Divide exponent by 2
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Step 1: Connect to Server1 (Alice)
            Socket server1Socket = new Socket("localhost", 5000);
            BufferedReader inFromServer1 = new BufferedReader(new InputStreamReader(server1Socket.getInputStream()));
            PrintWriter outToServer1 = new PrintWriter(server1Socket.getOutputStream(), true);
            System.out.print("Enter private key1: ");
            int pvtKeyHack1 = sc.nextInt();
            System.out.print("Enter private key1: ");
            int pvtKeyHack2 = sc.nextInt();
           // System.out.println("----------------------------------------");
            // Receive (prime, primitive root, Alice's public key)
            String dataFromServer1 = inFromServer1.readLine();
            //System.out.println("Received from Server1: " + dataFromServer1);

            String[] parts = dataFromServer1.split(" ");
            if (parts.length != 3) {
                System.out.println("Error: Server1 must send 3 integers (prime, primitive root, public key).");
                return;
            }

            int prime = Integer.parseInt(parts[0]);  // Prime number
            int primitiveRoot = Integer.parseInt(parts[1]);  // Primitive root
            int publicKeyAlice = Integer.parseInt(parts[2]);  // Alice's public key

            // Compute Server2's public key
            int publicKeyHack1 = modExp(primitiveRoot, pvtKeyHack1, prime);
            int publicKeyHack2 = modExp(primitiveRoot, pvtKeyHack2, prime);
            outToServer1.println(publicKeyHack2);  // Step 1: Send public key to Server1 (Alice)
            System.out.println("----------------------------------------");
            System.out.println("Sent Public Key to Alice: " + publicKeyHack2);

            // Step 2: Setup Server2 as a server and wait for Client (Bob)
            ServerSocket serverSocket = new ServerSocket(6000);
            //System.out.println("Server2 (Hack) waiting for Client to connect...");

            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                System.out.println("Client connected!");

                // Step 2: Send (prime, primitive root, Server2's public key) to Client (Bob)
                outToClient.println(prime + " " + primitiveRoot + " " + publicKeyHack1);
                System.out.println("----------------------------------------");
                System.out.println("Sent Public Key to Bob: " + publicKeyHack1);

                // Step 3: Receive Client's public key
                int publicKeyClient = Integer.parseInt(inFromClient.readLine());
                System.out.println("----------------------------------------");
                System.out.println("Received Bob's Public Key: " + publicKeyClient);

                // Step 4: Send Client's public key to Server1 (Alice)
                outToServer1.println(publicKeyClient);

                // Compute Shared Secret Key
                int sharedSecret1 = modExp(publicKeyClient, pvtKeyHack1, prime);
                int sharedSecret2 = modExp(publicKeyAlice, pvtKeyHack2, prime);

                System.out.println("----------------------------------------");
                System.out.println("Shared Secret Key with Alice: " + sharedSecret2);
                System.out.println("Shared Secret Key with Bob: " + sharedSecret1);
            }

            // Closing connections
            server1Socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
