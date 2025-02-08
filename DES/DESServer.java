package DES;

import java.io.*;
import java.net.*;

public class DESServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("[SERVER] Waiting for client...");
        
        Socket socket = serverSocket.accept();
        System.out.println("[SERVER] Client connected.");
        
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String receivedMessage = in.readUTF();
        System.out.println("[SERVER] Encrypted message received: " + receivedMessage);
        
        // Use same key as client
        String key = "12345678"; // Key should be same
        String decryptedMessage = DES.decrypt(receivedMessage, key);
        
        System.out.println("[SERVER] Decrypted message: " + decryptedMessage);
        
        socket.close();
        serverSocket.close();
    }
}
