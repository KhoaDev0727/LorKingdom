/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class UploadImage {

//    private static final String UPLOAD_DIR = "images";
//    private static final String UPLOAD_DIR = "D:/Uploads";

    public static String uploadFile(Part filePart, String uploadPath, String folder) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null; // Không có file được tải lên
        }
        // Kiểm tra và tạo thư mục nếu chưa có
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("Folder created: " + created + " at " + uploadDir.getAbsolutePath());
        }
        // Lấy tên file gốc và đổi tên tránh trùng lặp
        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = originalName.substring(originalName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        // Đường dẫn file
        String filePath = uploadPath + File.separator + newFileName;
        filePart.write(filePath);
        System.out.println("File saved at: " + filePath);

        // Trả về đường dẫn ảnh để lưu vào database
        return folder + "/" + newFileName;
    }
}
