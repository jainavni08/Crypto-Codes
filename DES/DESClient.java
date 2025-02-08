package DES;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DESClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("[CLIENT] Enter message (max 8 characters): ");
        String message = scanner.nextLine();
        
        System.out.print("[CLIENT] Enter 8-character key: ");
        String key = scanner.nextLine();
        
        String encryptedMessage = DES.encrypt(message, key);
        
        Socket socket = new Socket("localhost", 5000);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(encryptedMessage);
        System.out.println("[CLIENT] Encrypted message sent: " + encryptedMessage);
        
        socket.close();
        scanner.close();
    }
}

