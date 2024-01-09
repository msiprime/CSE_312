package chat_application_project.model;
import java.nio.charset.StandardCharsets;

public class StufferDeStufferCrcChecker {
    public String messageToBinary(String message) {
        byte[] byteArray = message.getBytes(StandardCharsets.UTF_8);
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            binaryStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return binaryStringBuilder.toString();
    }

    public String binaryToMessage(String binaryString) {
        // Check if the binary string has a valid length (multiple of 8).
        if (binaryString.length() % 8 != 0) {
            throw new IllegalArgumentException("Invalid binary string length");
        }
        // Split the binary string into 8-bit chunks.
        String[] binaryChunks = binaryString.split("(?<=\\G.{8})");

        // Convert each chunk to a byte and build the message string.
        byte[] byteArray = new byte[binaryChunks.length];
        for (int i = 0; i < binaryChunks.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(binaryChunks[i], 2);
        }

        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public String stuffing(String input) {

        StringBuilder sb = new StringBuilder();
        System.out.println("Start Stuffing...");
        int count = 0;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '1') {
                count++;
                sb.append(input.charAt(i));
                if (count == 5) {
                    sb.append('0');
                    count = 0;
                }
            } else if (input.charAt(i) == '0') {
                sb.append('0');
                count = 0;
            }
        }
        System.out.print("Stuffed:");
        System.out.print(sb);
        System.out.println("\nStart De-stuffing...");
        count = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '1') {
                count++;
                if (count == 5) {
                    if (i + 1 < sb.length() && sb.charAt(i + 1) == '0') {
                        sb.deleteCharAt(i + 1);
                    }
                    count = 0;
                }
            } else {
                count = 0;
            }
        }
        System.out.println(sb);
        return sb.toString();
    }

}

