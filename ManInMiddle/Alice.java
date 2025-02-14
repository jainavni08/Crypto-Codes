package ManInMiddle;

import java.io.*;
import java.net.*;
import java.util.*;

public class Alice {
    // Function to compute (base^exp) % mod using fast exponentiation
    static int powerMod(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) // If exp is odd, multiply base with result
                result = (result * base) % mod;
            exp = exp >> 1; // Divide exp by 2
            base = (base * base) % mod;
        }
        return result;
    }

    // Function to check if a number is prime
    static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Function to find all prime factors of a given number
    static Set<Integer> findPrimeFactors(int n) {
        Set<Integer> factors = new HashSet<>();
        while (n % 2 == 0) {
            factors.add(2);
            n /= 2;
        }
        for (int i = 3; i * i <= n; i += 2) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 1) factors.add(n);
        return factors;
    }

    // Function to find all primitive roots of a prime number p
    static List<Integer> findPrimitiveRoots(int p) {
        List<Integer> roots = new ArrayList<>();
        int phi = p - 1; // Euler’s totient function (φ(p) = p-1 for prime p)
        Set<Integer> factors = findPrimeFactors(phi);

        for (int g = 2; g < p; g++) {
            boolean isPrimitive = true;
            for (int factor : factors) {
                if (powerMod(g, phi / factor, p) == 1) {
                    isPrimitive = false;
                    break;
                }
            }
            if (isPrimitive)
                roots.add(g);
        }
        return roots;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Alice (Server1) waiting for Hack (Server2) to connect...");
            try (Socket server2Socket = serverSocket.accept()) {
                System.out.println("Connected to Hack (Server2)!");

                // 1. Input Prime Number
                int p;
                while (true) {
                    System.out.print("Enter a prime number: ");
                    p = sc.nextInt();
                    if (isPrime(p)) break;
                    System.out.println("Invalid input! Enter a valid prime number.");
                }

                // 2. Find and Choose Primitive Root
                List<Integer> primitiveRoots = findPrimitiveRoots(p);
                if (primitiveRoots.isEmpty()) {
                    System.out.println("No primitive roots found. Please enter a prime number.");
                    return;
                }

                System.out.println("Available primitive roots of " + p + ": " + primitiveRoots);
                int root;
                while (true) {
                    System.out.print("Select a primitive root from the list: ");
                    root = sc.nextInt();
                    if (primitiveRoots.contains(root)) break;
                    System.out.println("Invalid choice. Select a valid primitive root.");
                }

                // 3. Input Private Key and Compute Public Key
                System.out.println("----------------------------------------");
                System.out.print("Enter private key for Alice: ");
                int privateKeyAlice = sc.nextInt();
                
                int publicKeyAlice = powerMod(root, privateKeyAlice, p);

                // 4. Send (Prime, Primitive Root, Public Key) to Hack (Server2)
                PrintWriter out = new PrintWriter(server2Socket.getOutputStream(), true);
                out.println(p + " " + root + " " + publicKeyAlice);
                System.out.println("Sent to Bob");

                // 5. Receive Server2's (Hack's) Public Key
                BufferedReader in = new BufferedReader(new InputStreamReader(server2Socket.getInputStream()));
                int publicKeyHack = Integer.parseInt(in.readLine());
                System.out.println("----------------------------------------");
                System.out.println("Received Bob's Public Key: " + publicKeyHack);
                System.out.println("----------------------------------------");
                int SecretKey = powerMod(publicKeyAlice, privateKeyAlice, p);
                System.out.println("Shared Secret Key (Alice): " + SecretKey);
            }
        } catch (IOException e) {
            System.err.println("Error in Alice (Server1): " + e.getMessage());
        }
    }
}
