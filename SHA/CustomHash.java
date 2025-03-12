package SHA;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CustomHash {

    // Define constants (same as before)
    private static final int[] INIT_STATE = {
        0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a
    };

    private static final int ROUNDS = 64;  // Number of rounds for processing each block

    public static void main(String[] args) {
        String input = "Hello, world! This is a test input for custom SHA-like hash.";
        byte[] hash = customHash(input.getBytes(StandardCharsets.UTF_8));
        
        System.out.println("Hash: " + bytesToHex(hash));
    }

    // Custom hash function
    public static byte[] customHash(byte[] input) {
        // Step 1: Padding the input
        byte[] paddedInput = padInput(input);

        // Step 2: Initialize the state (hash values)
        int[] state = INIT_STATE.clone();

        // Step 3: Process the message in 512-bit blocks
        for (int i = 0; i < paddedInput.length / 64; i++) {
            // Get the next 512-bit block (64 bytes)
            byte[] block = new byte[64];
            System.arraycopy(paddedInput, i * 64, block, 0, 64);

            // Step 3a: Create the message schedule for the block
            int[] messageSchedule = createMessageSchedule(block);

            // Step 3b: Process the block through rounds
            for (int round = 0; round < ROUNDS; round++) {
                processBlock(state, messageSchedule, round);
            }
        }

        // Step 4: Return the final hash as 128-bit (16 bytes)
        byte[] finalHash = new byte[16];
        ByteBuffer buffer = ByteBuffer.wrap(finalHash);
        for (int i = 0; i < 4; i++) {
            buffer.putInt(state[i]);
        }

        return finalHash;
    }

    // Convert byte array to hex string (for easy display)
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Padding the input message to ensure it's a multiple of 512 bits (64 bytes)
    private static byte[] padInput(byte[] input) {
        int length = input.length;
        int paddingLength = 64 - ((length + 8) % 64);  // 8 bytes for length

        // Total length after padding (length + padding + 8 bytes for original length)
        int totalLength = length + paddingLength + 8;

        byte[] paddedInput = new byte[totalLength];
        System.arraycopy(input, 0, paddedInput, 0, length);

        // Add '1' bit (0x80 byte)
        paddedInput[length] = (byte) 0x80;

        // Add '0' bits (0x00 bytes)
        for (int i = length + 1; i < totalLength - 8; i++) {
            paddedInput[i] = 0x00;
        }

        // Append the length of the original message (in bits) as a 64-bit integer
        long bitLength = (long) length * 8;
        ByteBuffer.wrap(paddedInput, totalLength - 8, 8).putLong(bitLength);

        return paddedInput;
    }

    // Create the message schedule (expand the block into words)
    private static int[] createMessageSchedule(byte[] block) {
        int[] schedule = new int[64];

        // Break the block into 16 32-bit words
        for (int i = 0; i < 16; i++) {
            schedule[i] = ByteBuffer.wrap(block, i * 4, 4).getInt();
        }

        // Extend the schedule (this is similar to how SHA-256 extends its schedule)
        for (int i = 16; i < 64; i++) {
            int temp = schedule[i - 16] + schedule[i - 7] + 
                       (rotateRight(schedule[i - 15], 7) ^ rotateRight(schedule[i - 15], 18) ^ (schedule[i - 15] >>> 3)) + 
                       (rotateRight(schedule[i - 2], 17) ^ rotateRight(schedule[i - 2], 19) ^ (schedule[i - 2] >>> 10));
            schedule[i] = temp;
        }

        return schedule;
    }

    // Perform bitwise operations (rotation to the right)
    private static int rotateRight(int value, int shift) {
        return (value >>> shift) | (value << (32 - shift));
    }

    // Process the block through rounds (update state)
    private static void processBlock(int[] state, int[] messageSchedule, int round) {
        // Using a simple round function to update state
        int temp = state[0] + (rotateRight(state[1], 5) + ((state[1] & state[2]) | (~state[1] & state[3]))) + state[2] + messageSchedule[round];

        // Update state variables based on the result of this round
        state[0] = state[1];
        state[1] = state[2];
        state[2] = rotateRight(state[3], 10);
        state[3] = temp;  // Only update the last state variable (index 3)
    }
}
