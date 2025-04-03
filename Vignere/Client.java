package Vignere;
import java.io.*;
import java.net.*;
import java.util.*;
public class Client {
    public static void main(String []args){
        try(Socket socket = new Socket("localhost",5000)){
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            Scanner sc = new Scanner(System.in);
            System.out.println("message : ");
            String msg = sc.nextLine();
            System.out.println("key : ");
            String key = sc.nextLine();
            String k = "";
            int i = 0;
            while(msg.length() != k.length()){
                k += key.charAt(i);
                i++;
                if(i == key.length()){
                    i = 0;
                }
            }
            System.out.println(k);
            String e = encrypt(msg,k);
            System.out.println("encrypted message : ");
            System.out.println(e);
            output.println(e);
            output.println(k);
            String d = input.readLine();
            System.out.println(d);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String encrypt(String msg,String key){
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
