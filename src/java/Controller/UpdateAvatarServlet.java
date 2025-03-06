package Controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import DBConnect.DBConnection;
import Model.Account;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class UpdateAvatarServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "avatars"; // Directory to store uploaded images

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get session to identify the user
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp"); // Redirect to login page if not logged in
            return;
        }

        Part filePart = request.getPart("avatar"); // Get file from form
        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("message", "Please select an image!");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }

        // Save image to server directory
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Generate a unique file name
        String fileName = "user_" + account.getAccountId() + "_" + System.currentTimeMillis() + ".png";
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // Save image path to database
        String imagePath = UPLOAD_DIR + "/" + fileName;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Account SET Image = ? WHERE AccountID = ?")) {
            
            stmt.setString(1, imagePath);
            stmt.setInt(2, account.getAccountId());
            stmt.executeUpdate();

            // Update session with new avatar
            account.setImage(imagePath);
            session.setAttribute("account", account);

            request.setAttribute("message", "Avatar updated successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("message", "Error updating avatar!");
        }

        // Redirect back to profile page
        request.getRequestDispatcher("Setting.jsp").forward(request, response);
    }
}
