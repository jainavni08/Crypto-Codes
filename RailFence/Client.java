package RailFence;
import java.util.*;
import java.net.*;
import java.io.*;
public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5000)){
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter message ");
            String msg = sc.nextLine();
            System.out.println("Enter row value ");
            int n  = Integer.parseInt(sc.nextLine());
            int l = msg.length()/n;
            output.println(Integer.toString(n));
            output.println(Integer.toString(l));
            //System.out.println(n + " " + l);
            int[][] e = encrypt(msg,n);
            print(e);
            for(int[] row : e){
                for(int x = 0;x < l;x++){
                    //System.out.println("Sent" + (char)(row[x]));
                    output.println(row[x]);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static int[][] encrypt(String msg,int n){
        int l = msg.length()/n;
        int a[][] = new int[n][l];
        int row  = 0;
        int col = 0;
        int i = 0;
        while(i != msg.length()){
            //System.out.println("row " + row + " col " + col);
            a[row][col] = msg.charAt(i);
            row++;
            if(row == n){
                row = 0;
                col++;
            }
            i++;
        }
        return a;
    }
    public static void print(int[][]a) {
        for(int i = 0 ; i < a.length ; i++){
            for(int j = 0; j < a[0].length ; j++){
                System.out.print((char)a[i][j] + " ");
            }
            System.out.println();
        }
    }
}
