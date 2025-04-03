package RailFence;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try(ServerSocket ss = new ServerSocket(5000)){
            Socket socket = ss.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            Scanner sc = new Scanner(System.in);
            int n = Integer.parseInt(input.readLine());
            int l = Integer.parseInt(input.readLine());
           System.out.println("Received row matrix :");
            int [][] d = new int[n][l];
            for(int[] row : d){
                for(int x = 0;x < l ;x++){
                    row[x] = Integer.parseInt(input.readLine());;
                }
            }
            print(d);
            String decrypted_msg = decrypt(d, n, l);
            System.out.println("decrypted message : " + decrypted_msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static String decrypt(int[][] a,int n,int l){
        String m = "";
        int row  = 0;
        int col = 0;
        while(col != l){
            m += (char)a[row][col];
            row++;
            if(row == n){
                row = 0;
                col++;
            }
        }
        return m;
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
