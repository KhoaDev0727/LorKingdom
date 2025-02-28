package Controller;

import DAO.AccountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username") != null ? request.getParameter("username").trim() : "";
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";
        String phoneNumber = request.getParameter("phone") != null ? request.getParameter("phone").trim() : "";

        String usernameError = "", emailError = "", passwordError = "", phoneNumberError = "";
        boolean hasError = false;

        // Kiểm tra dữ liệu đầu vào
        if (username.isEmpty()) {
            usernameError = "Tên người dùng là bắt buộc.";
            hasError = true;
        }
        if (email.isEmpty()) {
            emailError = "Cần phải có email.";
            hasError = true;
        } else if (!isValidEmail(email)) {
            emailError = "Định dạng email không hợp lệ.";
            hasError = true;
        }
        if (password.isEmpty()) {
            passwordError = "Cần phải nhập mật khẩu.";
            hasError = true;
        } else if (password.length() < 6) {
            passwordError = "Mật khẩu phải dài ít nhất 6 ký tự.";
            hasError = true;
        }
        if (phoneNumber.isEmpty()) {
            phoneNumberError = "Bắt buộc phải nhập số điện thoại.";
            hasError = true;
        } else if (!phoneNumber.matches("\\d{10}")) {
            phoneNumberError = "Số điện thoại phải có 10 chữ số.";
            hasError = true;
        }

        // Kiểm tra email và số điện thoại đã tồn tại chưa
        AccountDAO accountDAO = new AccountDAO();
        try {
            if (!hasError && accountDAO.isEmailExists(email)) {
                emailError = "Email này đã được sử dụng.";
                hasError = true;
            }
            if (!hasError && accountDAO.isPhoneNumberExists(phoneNumber)) {
                phoneNumberError = "Số điện thoại này đã được sử dụng.";
                hasError = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            emailError = "Lỗi khi kiểm tra email hoặc số điện thoại. Vui lòng thử lại.";
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("usernameError", usernameError);
            request.setAttribute("emailError", emailError);
            request.setAttribute("passwordError", passwordError);
            request.setAttribute("phoneNumberError", phoneNumberError);
            request.setAttribute("usernameValue", username);
            request.setAttribute("emailValue", email);
            request.setAttribute("phoneValue", phoneNumber);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Hash mật khẩu trước khi lưu vào session
        String hashedPassword = MyUtils.hashPassword(password);

        // Lưu thông tin tạm thời vào session
        HttpSession session = request.getSession();
        session.setAttribute("tempUsername", username);
        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPassword", hashedPassword); // Lưu mật khẩu đã hash
        session.setAttribute("tempPhoneNumber", phoneNumber);
        
        response.sendRedirect("SendVerificationServlet");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}