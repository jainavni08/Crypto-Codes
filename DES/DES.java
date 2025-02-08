package DES;

import java.util.*;

public class DES {
    private static final int[] IP = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };

    // Expansion table (E-box)
    private static final int[] E = {
        32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
        12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29,
        30, 31, 32, 1
    };

    // Permutation function
    public static String permute(String input, int[] table) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            int index = table[i] - 1;  // Convert 1-based index to 0-based
            if (index < input.length()) {
                output.append(input.charAt(index));
            }
        }
        return output.toString();
    }

    // DES encryption function
    public static String encrypt(String plaintext, String key) {
        String binaryMessage = stringToBinary(plaintext, 64);
        binaryMessage = permute(binaryMessage, IP);
        return permute(binaryMessage, FP);
    }

    // DES decryption function
    public static String decrypt(String ciphertext, String key) {
        String binaryMessage = permute(ciphertext, IP);
        binaryMessage = permute(binaryMessage, FP);
        return binaryToString(binaryMessage);
    }

    // Convert text to binary
    public static String stringToBinary(String text, int length) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        while (binary.length() < length) {
            binary.append("0");  // Padding to 64-bit
        }
        return binary.toString();
    }

    // Convert binary to text
    public static String binaryToString(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            text.append((char) Integer.parseInt(binary.substring(i, i + 8), 2));
        }
        return text.toString();
    }
}


