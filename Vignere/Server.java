package Vignere;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String []args){
        try(ServerSocket ss = new ServerSocket(5000)){
            Socket socket = ss.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            
            String msg = input.readLine();
            System.out.println("message : " + msg);
            String key = input.readLine();
            System.out.println("key : " + key);
            String d = decrypt(msg,key);
            System.out.println("decrypted message : " + d);
            output.println(d);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static String decrypt(String msg,String key){
        StringBuilder result = new StringBuilder();
        for(int i = 0 ; i < msg.length() ; i++){
            int m= msg.charAt(i) - 'A';
            int k = key.charAt(i) - 'A';
            int d = (m^k) % 26;
            char c = (char) (d + 'A');
            result.append(c);
        }
        return result.toString();
    }
}
