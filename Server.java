// DSAServer.java
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Server {
    private static final Random random = new Random();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is running... Waiting for client connection.");

            try (Socket socket = serverSocket.accept();
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 Scanner scanner = new Scanner(System.in)) {

                System.out.println("Client connected.");

                // Get DSA parameters from user
                System.out.print("Enter p: ");
                BigInteger p = new BigInteger(scanner.nextLine());

                System.out.print("Enter q: ");
                BigInteger q = new BigInteger(scanner.nextLine());

                System.out.print("Enter g: ");
                BigInteger g = new BigInteger(scanner.nextLine());

                // Generate keys
                BigInteger x = generatePrivateKey(q); // Private key (1 ≤ x ≤ q-1)
                BigInteger y = g.modPow(x, p); // Public key

                // Get message from user
                System.out.print("Enter the message: ");
                String message = scanner.nextLine();
                BigInteger h = new BigInteger(message.getBytes()); // Hash of message (message itself)

                // Generate signature
                BigInteger k, r, s;
                do {
                    k = generatePrivateKey(q);
                    r = g.modPow(k, p).mod(q);
                } while (r.equals(BigInteger.ZERO));

                s = k.modInverse(q).multiply(h.add(x.multiply(r))).mod(q);

                if (s.equals(BigInteger.ZERO)) {
                    System.out.println("Error: s is zero. Signature generation failed.");
                    return;
                }

                // Send parameters & signature to client
                out.writeObject(p);
                out.writeObject(q);
                out.writeObject(g);
                out.writeObject(y);
                out.writeObject(r);
                out.writeObject(s);
                out.flush();

                System.out.println("Message sent.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BigInteger generatePrivateKey(BigInteger q) {
        BigInteger one = BigInteger.ONE;
        BigInteger qMinusOne = q.subtract(one);
        BigInteger randomBigInt;
        do {
            randomBigInt = new BigInteger(q.bitLength(), random);
        } while (randomBigInt.compareTo(one) < 0 || randomBigInt.compareTo(qMinusOne) > 0);
        return randomBigInt;
    }
}

