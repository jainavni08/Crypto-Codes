package ManInMiddle;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Bob {
    // Function to compute (base^exp) % mod using fast modular exponentiation
    public static int powerMod(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) // If exp is odd, multiply base with result
                result = (result * base) % mod;
            exp = exp >> 1; // Divide exp by 2
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            // Read the three values from Server2 (Hack.java)
            String receivedData = in.readLine();
            String[] values = receivedData.split(" ");
            
            // Extract Prime, Primitive Root, and Alice’s Public Key
            int prime = Integer.parseInt(values[0]);
            int root = Integer.parseInt(values[1]);
            int publicKeyAlice = Integer.parseInt(values[2]);
            System.out.println("----------------------------------------");
            System.out.println("Received from Alice:");
            System.out.println("Prime number (p): " + prime);
            System.out.println("Primitive root (g): " + root);
            System.out.println("Alice's Public Key (A): " + publicKeyAlice);
            System.out.println("----------------------------------------");
            // Ask for Bob's private key
            System.out.print("Enter private key for Bob: ");
            int privateKeyBob = sc.nextInt();
            System.out.println("----------------------------------------");
            // Compute Bob's Public Key
            int publicKeyBob = powerMod(root, privateKeyBob, prime);
            System.out.println("Bob's Public Key (B): " + publicKeyBob);

            // Send Bob's Public Key back to Alice
            out.println(publicKeyBob);
            System.out.println("Sent Bob's Public Key to Alice!");
            System.out.println("----------------------------------------");
            // Compute Shared Secret Key using Alice’s Public Key
            int sharedSecret = powerMod(publicKeyAlice, privateKeyBob, prime);
            System.out.println("Shared Secret Key (Bob) : " + sharedSecret);

        } catch (IOException e) {
            System.err.println("Error in Bob (Server3): " + e.getMessage());
        }
    }
}
