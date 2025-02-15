/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class UploadImageProduct {

    public String generateUniqueFileName(Part part) {
        String originalName = getSubmittedFileName(part);
        return UUID.randomUUID().toString() + "_" + originalName;
    }

   public String saveFile(Part part, String uploadPath, String fileName) throws IOException {
        String filePath = uploadPath + File.separator + fileName;
        part.write(filePath);
        return "/uploads/" + fileName;
    }

    public static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
}
