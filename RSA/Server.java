package RSA;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is waiting for a connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Receive data from the client
            int p = in.readInt();
            int q = in.readInt();
            int e = in.readInt();
            int length = in.readInt();
            int[] encryptedMessage = new int[length];
            for (int i = 0; i < length; i++) {
                encryptedMessage[i] = in.readInt();
            }

            System.out.print("Received encrypted message: ");
            for (int num : encryptedMessage) {
                System.out.print(num + " ");
            }
            System.out.println();

            // Calculate private key d
            int phi_n = (p - 1) * (q - 1);
            int d = calculatePrivateKey(phi_n, e);
            System.out.println("Calculated private key d: " + d);

            // Decrypt message
            int[] decryptedMessage = decrypt(encryptedMessage, p, q, d);
            System.out.print("Decrypted message: ");
            for (int num : decryptedMessage) {
                System.out.print(num + " ");
            }
            // Send decrypted message back to the client
            out.writeInt(decryptedMessage.length);
            for (int num : decryptedMessage) {
                out.writeInt(num);
            }

            // Close connections
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] decrypt(int[] cipher, int p, int q, int d) {
        int n = p * q;
        int[] plainText = new int[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            plainText[i] = sqAndMul(cipher[i], d, n);
        }
        return plainText;
    }

    public static int sqAndMul(int a, int exp, int n) {
        int f = 1;
        String binary = Integer.toBinaryString(exp);
        System.out.println("BINARY FORM " + binary);
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
        }
        System.out.println("result of " + a + "^ " + exp + "mod " + n +"= " +f );
        return f;
    }

    public static int extendedGCD(int a, int b, int[] xy) {
        if (a == 0) {
            xy[0] = 0;
            xy[1] = 1;
            return b;
        }

        int[] tempXY = new int[2];
        int gcd = extendedGCD(b % a, a, tempXY);

        xy[0] = tempXY[1] - (b / a) * tempXY[0];
        xy[1] = tempXY[0];

        return gcd;
    }

    public static int calculatePrivateKey(int phi_n, int e) {
        int[] xy = new int[2];
        int gcd = extendedGCD(e, phi_n, xy);
        int d = xy[0];

        // Ensure d is positive
        if (d < 0) {
            d += phi_n;
        }
        return d;
    }
}

