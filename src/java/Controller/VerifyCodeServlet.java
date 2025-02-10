package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import DBConnect.DBConnection;

public class VerifyCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enteredCode = request.getParameter("code");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("verificationCode") == null || session.getAttribute("codeExpiryTime") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String actualCode = (String) session.getAttribute("verificationCode");
        Long expiryTime = (Long) session.getAttribute("codeExpiryTime");
        long currentTime = System.currentTimeMillis();

        if (currentTime > expiryTime) {
            // Mã đã hết hạn
            session.removeAttribute("verificationCode");
            session.removeAttribute("codeExpiryTime");
            request.setAttribute("errorMessage", "Verification code has expired. Please resend a new code.");
            request.getRequestDispatcher("verifyCode.jsp").forward(request, response);
            return;
        }

        if (enteredCode != null && enteredCode.equals(actualCode)) {
            // Xác thực thành công, loại bỏ mã xác minh khỏi session
            session.removeAttribute("verificationCode");
            session.removeAttribute("codeExpiryTime");
            session.setAttribute("isVerified", true);

            // Lưu thông tin người dùng vào database
            String username = (String) session.getAttribute("tempUsername");
            String email = (String) session.getAttribute("tempEmail");
            String password = (String) session.getAttribute("tempPassword");
            String phoneNumber = (String) session.getAttribute("tempPhoneNumber");

            if (username != null && email != null && password != null && phoneNumber != null) {
                saveUserToDatabase(username, email, password, phoneNumber);

                // Xóa thông tin tạm khỏi session
                session.removeAttribute("tempUsername");
                session.removeAttribute("tempEmail");
                session.removeAttribute("tempPassword");
                session.removeAttribute("tempPhoneNumber");
            }

            response.sendRedirect("verifyCode.jsp?status=success");
        } else {
            // Xác thực thất bại
            request.setAttribute("errorMessage", "Verification code is incorrect or empty. Please try again.");
            request.getRequestDispatcher("verifyCode.jsp").forward(request, response);
        }
    }

    private void saveUserToDatabase(String username, String email, String password, String phoneNumber) {
        String sql = "INSERT INTO Account (RoleID, AccountName, Email, Password, PhoneNumber, Image) VALUES (?, ?, ?, ?, ?, ?)";
        String defaultImageUrl = "./assets/img/default-img-profile.png";

        int defaultRoleID = 3; 
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, defaultRoleID);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, phoneNumber);
            stmt.setString(6, defaultImageUrl);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
