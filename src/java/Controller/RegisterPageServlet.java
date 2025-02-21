package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class RegisterPageServlet extends HttpServlet {

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
            usernameError = "Username is required.";
            hasError = true;
        }
        if (email.isEmpty()) {
            emailError = "Email is required.";
            hasError = true;
        } else if (!isValidEmail(email)) {
            emailError = "Invalid email format.";
            hasError = true;
        }
        if (password.isEmpty()) {
            passwordError = "Password is required.";
            hasError = true;
        } else if (password.length() < 6) {
            passwordError = "Password must be at least 6 characters long.";
            hasError = true;
        }
        if (phoneNumber.isEmpty()) {
            phoneNumberError = "Phone number is required.";
            hasError = true;
        } else if (!phoneNumber.matches("\\d{10}")) {
            phoneNumberError = "Phone number must be 10 digits.";
            hasError = true;
        }

        if (hasError) {
            // Gửi lỗi về lại trang đăng ký
            request.setAttribute("usernameError", usernameError);
            request.setAttribute("emailError", emailError);
            request.setAttribute("passwordError", passwordError);
            request.setAttribute("phoneNumberError", phoneNumberError);
            request.setAttribute("usernameValue", username);
            request.setAttribute("emailValue", email);
            request.setAttribute("phoneValue", phoneNumber);
            request.getRequestDispatcher("registerPage.jsp").forward(request, response);
            return;
        }

        // Lưu thông tin tạm thời vào session
        HttpSession session = request.getSession();
        session.setAttribute("tempUsername", username);
        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPassword", password);
        session.setAttribute("tempPhoneNumber", phoneNumber);
        
        response.sendRedirect("SendVerificationPage");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}
