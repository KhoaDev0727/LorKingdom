/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class MyUtils {
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
   
}
