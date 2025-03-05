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
            request.setAttribute("message", "Mật khẩu không được để trống.");
        } else if (newPassword.length() < 6) {
            request.setAttribute("message", "Mật khẩu phải có ít nhất 6 ký tự.");
        } else if (!newPassword.matches("[a-zA-Z0-9]*")) {  // Chỉ chấp nhận chữ và số
            request.setAttribute("message", "Mật khẩu không được chứa ký tự đặc biệt.");
        } else {
            AccountDAO accountDAO = new AccountDAO();

            // Kiểm tra token hợp lệ
            String email = accountDAO.validateResetToken(token);

            if (email == null) {
                request.setAttribute("message", "Mã không hợp lệ hoặc đã hết hạn.");
            } else {
                // Cập nhật mật khẩu vào database
                if (accountDAO.updatePassword(email, newPassword)) {
                    request.setAttribute("message", "Mật khẩu đã được đặt lại thành công.");
                } else {
                    request.setAttribute("message", "Không thể đặt lại mật khẩu.");
                }
            }
        }

        // Chuyển hướng về trang resetPassword.jsp
        request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
    }
}