package RSA;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000); // Connect to server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            
            Scanner sc = new Scanner(System.in);

            // Taking user input
            System.out.print("Enter prime number p: ");
            int p = sc.nextInt();
            System.out.print("Enter prime number q: ");
            int q = sc.nextInt();
            int phi_n = eulerTotient(p, q);
            int e;
        while (true) {
            System.out.println("Enter public key e (must satisfy gcd(e, φ(n)) = 1): ");
            e = sc.nextInt();

            if (gcd(e, phi_n) == 1) {
                break; // e is valid, exit loop
            } else {
                System.out.println("Invalid e! gcd(e, φ(n)) ≠ 1. Try again.");
            }
        }

        System.out.println("Valid public key e: " + e);
            System.out.print("Enter message: ");
            sc.nextLine(); // Consume newline
            String message = sc.nextLine();

            // Encrypt the message
            int[] encryptedMessage = encrypt(message, p, q, e);
            System.out.print("Encrypted message: ");
            for (int num : encryptedMessage) {
                System.out.print(num + " ");
            }
            System.out.println();

            // Send data to the server
            out.writeInt(p);
            out.writeInt(q);
            out.writeInt(e);
            out.writeInt(encryptedMessage.length);
            for (int num : encryptedMessage) {
                out.writeInt(num);
            }

            // Receive decrypted message from server
            int length = in.readInt();
            StringBuilder decryptedMessage = new StringBuilder();
            for (int i = 0; i < length; i++) {
                decryptedMessage.append((char) in.readInt());
            }

            System.out.println("Decrypted message from server: " + decryptedMessage);

            // Close connections
            sc.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] encrypt(String message, int p, int q, int e) {
        char[] chars = message.toCharArray();
        int n = p * q;
        //System.out.print("ASCII message: ");
        int[] cipher = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            int x = (int) chars[i];
            System.out.print(x + " ");
            cipher[i] = sqAndMul(x, e, n);
        }
        return cipher;
    }

    public static int sqAndMul(int a, int exp, int n) {
        int f = 1;
        String binary = Integer.toBinaryString(exp);
       // System.out.println("BINARY FORM " + binary);
        char []b = binary.toCharArray();
        for(int i  = 0; i < b.length ; i++){
            if(b[i] == '1'){
                int x = f*f*a;
                x = x % n;
                f = x;
            }else{
                int y = f * f;
                y = y % n;
                f = y;
            }
            //System.out.println("F VALUE AFTER " +b[i] + " is " +f );
        }
       // System.out.println("result of " + a + "^ " + exp + "mod " + n +"= " +f );
        return f;
    }
    public static int eulerTotient(int p, int q) {
        return (p - 1) * (q - 1);
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}

