package Controller;

import DAO.AccountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.json.JSONObject;

public class RegisterPageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json"); // Thiết lập response là JSON
        JSONObject jsonResponse = new JSONObject();

        String username = request.getParameter("username") != null ? request.getParameter("username").trim() : "";
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";
        String phoneNumber = request.getParameter("phone") != null ? request.getParameter("phone").trim() : "";
        String role = request.getParameter("role") != null ? request.getParameter("role").trim() : "";

        String usernameError = "", emailError = "", passwordError = "", phoneNumberError = "", roleError = "";
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
        
        // Kiểm tra mật khẩu với regex
        if (password.isEmpty()) {
            passwordError = "Cần phải nhập mật khẩu.";
            hasError = true;
        } else if (!isValidPassword(password)) {
            passwordError = "Mật khẩu phải dài ít nhất 8 ký tự, chứa ít nhất 1 chữ cái in hoa, " +
                          "1 chữ cái thường, 1 số và 1 ký tự đặc biệt.";
            hasError = true;
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberError = "Bắt buộc phải nhập số điện thoại.";
            hasError = true;
        } else if (!phoneNumber.matches("\\d{10}")) {
            phoneNumberError = "Số điện thoại phải có 10 chữ số.";
            hasError = true;
        }
        if (role.isEmpty() || (!role.equals("2") && !role.equals("4"))) {
            roleError = "Vui lòng chọn vai trò hợp lệ.";
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
            // Trả về JSON chứa thông tin lỗi
            jsonResponse.put("success", false);
            jsonResponse.put("usernameError", usernameError);
            jsonResponse.put("emailError", emailError);
            jsonResponse.put("passwordError", passwordError);
            jsonResponse.put("phoneNumberError", phoneNumberError);
            jsonResponse.put("roleError", roleError);
            jsonResponse.put("usernameValue", username);
            jsonResponse.put("emailValue", email);
            jsonResponse.put("phoneValue", phoneNumber);
            jsonResponse.put("roleValue", role);
        } else {
            // Hash mật khẩu trước khi lưu vào session
            String hashedPassword = MyUtils.hashPassword(password);

            // Lưu thông tin tạm thời vào session
            HttpSession session = request.getSession();
            session.setAttribute("tempUsername", username);
            session.setAttribute("tempEmail", email);
            session.setAttribute("tempPassword", hashedPassword);
            session.setAttribute("tempPhoneNumber", phoneNumber);
            session.setAttribute("tempRole", role);

            // Trả về JSON thành công
            jsonResponse.put("success", true);
        }

        // Gửi JSON response
        response.getWriter().write(jsonResponse.toString());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Hàm kiểm tra mật khẩu với regex
    private boolean isValidPassword(String password) {
        // Regex: 
        // ^                 : Bắt đầu chuỗi
        // (?=.*[A-Z])       : Ít nhất 1 chữ cái in hoa
        // (?=.*[a-z])       : Ít nhất 1 chữ cái thường
        // (?=.*\\d)         : Ít nhất 1 số
        // (?=.*[!@#$%^&*])  : Ít nhất 1 ký tự đặc biệt
        // .{8,}             : Dài ít nhất 8 ký tự
        // $                 : Kết thúc chuỗi
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
        return password.matches(passwordPattern);
    }
}