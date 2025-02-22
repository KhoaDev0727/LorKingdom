package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import DAO.AccountDAO;

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
            AccountDAO accountDAO = new AccountDAO();

            // Kiểm tra token hợp lệ
            String email = accountDAO.validateResetToken(token);

            if (email == null) {
                request.setAttribute("message", "Invalid or expired token.");
            } else {
                // Cập nhật mật khẩu vào database
                if (accountDAO.updatePassword(email, newPassword)) {
                    request.setAttribute("message", "Password has been reset successfully.");
                } else {
                    request.setAttribute("message", "Failed to reset password.");
                }
            }
        }

        // Chuyển hướng về trang resetPassword.jsp
        request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
    }
}