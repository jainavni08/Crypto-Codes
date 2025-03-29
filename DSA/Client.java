package DSA;
// Client.java
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server. Receiving data...");

            // Receive parameters & signature from server
            BigInteger p = (BigInteger) in.readObject();
            BigInteger q = (BigInteger) in.readObject();
            BigInteger g = (BigInteger) in.readObject();
            BigInteger y = (BigInteger) in.readObject();
            BigInteger r = (BigInteger) in.readObject();
            BigInteger s = (BigInteger) in.readObject();

            System.out.println("Data received from server.");
            System.out.println("p = " + p);
            System.out.println("q = " + q);
            System.out.println("g = " + g);
            System.out.println("y = " + y);
            System.out.println("r = " + r);
            System.out.println("s = " + s);

            if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0 ||
                s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) {
                System.out.println("Invalid signature: r or s is out of range.");
                return;
            }

            // Get message input from user
            System.out.print("\nEnter the original message: ");
            String message = scanner.nextLine();
            BigInteger h = new BigInteger(message.getBytes()); // Hash of message (message itself)

            // Verify signature
            BigInteger w = s.modInverse(q);
            BigInteger u1 = h.multiply(w).mod(q);
            BigInteger u2 = r.multiply(w).mod(q);
            BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

            System.out.println("\nVerification Result:");
            if (v.equals(r)) {
                System.out.println("Signature is VALID ");
            } else {
                System.out.println("Signature is INVALID ");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
