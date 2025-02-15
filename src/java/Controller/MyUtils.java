/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class MyUtils {

    /**
     * Hashes a password using the SHA-256 algorithm.
     *
     * This method converts the input password into a hashed hexadecimal string
     * using the SHA-256 cryptographic hashing algorithm.
     *
     * @param password The plain text password to be hashed.
     * @return A hexadecimal string representation of the hashed password.
     * @throws RuntimeException If the SHA-256 algorithm is not available.
     */
    public static String hashPassword(String password) {
        try {
            // Use SHA-256 hashing algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Get bytes from the password string using UTF-8 encoding
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            // Return the hashed password as a hexadecimal string
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle error if the hashing algorithm is not found
            throw new RuntimeException("Error hashing password", e);
        }
    }
    public static String generateProductID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder("PRD");
        Random random = new Random();
        
        // Tạo 5 ký tự ngẫu nhiên
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }
        
        return id.toString();
    }

}
