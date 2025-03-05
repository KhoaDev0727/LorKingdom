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
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class UpdateAvatarPage extends HttpServlet {

    private static final String UPLOAD_DIR = "avatars"; // Directory to store uploaded images
    private static final String FOLDER = "avatars";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get session to identify the user
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("loginPage.jsp"); // Redirect to login page if not logged in
            return;
        }

        Part filePart = request.getPart("avatar"); // Get file from form
        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("message", "Please select an image!");
            request.getRequestDispatcher("profileStaff.jsp").forward(request, response);
            return;
        }
        // Save image to server directory
        String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
        String oldImage = request.getParameter("currentImage").trim();
        String image = (filePart.getSize() > 0)
                ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                : oldImage;
        System.out.println(image);
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement("UPDATE Account SET Image = ? WHERE AccountID = ?")) {

            stmt.setString(1, image);
            stmt.setInt(2, account.getAccountId());
            stmt.executeUpdate();

            // Update session with new avatar
            account.setImage(image);
            session.setAttribute("account", account);

            request.setAttribute("message", "Avatar updated successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("message", "Error updating avatar!");
        }

        // Redirect back to profile page
        request.getRequestDispatcher("profileStaff.jsp").forward(request, response);
    }
}
