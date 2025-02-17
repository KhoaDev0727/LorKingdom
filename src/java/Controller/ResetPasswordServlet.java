package Controller;

import DBConnect.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResetPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        // Kiểm tra mật khẩu
        if (newPassword.isEmpty()) {
            request.setAttribute("message", "Password cannot be empty.");
        } else if (newPassword.length() < 6) {
            request.setAttribute("message", "Password must be at least 6 characters.");
        } else if (!newPassword.matches("[a-zA-Z0-9]*")) {  // Chỉ chấp nhận chữ và số
            request.setAttribute("message", "Password cannot contain special characters.");
        } else {
            try (Connection conn = DBConnection.getConnection()) {
                // Kiểm tra token hợp lệ
                PreparedStatement ps = conn.prepareStatement("SELECT Email, TokenExpiry FROM Account WHERE ResetToken = ?");
                ps.setString(1, token);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    long expiryTime = rs.getLong("TokenExpiry");
                    if (System.currentTimeMillis() > expiryTime) {
                        request.setAttribute("message", "Token expired.");
                    } else {
                        // Cập nhật mật khẩu vào database mà không hash
                        ps = conn.prepareStatement("UPDATE Account SET Password = ?, ResetToken = NULL, TokenExpiry = NULL WHERE ResetToken = ?");
                        ps.setString(1, newPassword);  // Không mã hóa mật khẩu
                        ps.setString(2, token);
                        int rowsAffected = ps.executeUpdate();

                        if (rowsAffected > 0) {
                            request.setAttribute("message", "Password has been reset successfully.");
                        } else {
                            request.setAttribute("message", "Failed to reset password.");
                        }
                    }
                } else {
                    request.setAttribute("message", "Invalid token.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", "An error occurred.");
            }
        }

        // Chuyển hướng về trang resetPassword.jsp và bắt đầu đếm ngược
        request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
    }
}
