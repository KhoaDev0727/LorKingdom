package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

public class SendVerificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("tempEmail") == null || session.getAttribute("tempUsername") == null) {
            response.sendRedirect("register.jsp");
            return;
        }

        // Lấy thông tin từ session
        String email = (String) session.getAttribute("tempEmail");
        String username = (String) session.getAttribute("tempUsername");
        String verificationCode = generateVerificationCode();

        long currentTime = System.currentTimeMillis();
        long expiryTime = currentTime + (2 * 60 * 1000);

        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("codeExpiryTime", expiryTime);

        String subject = "Xác minh email - LorKingdom";
        String bannerImageURL = "https://i.imgur.com/Jy8NzTM.jpeg";
        String logoImageURL = "https://i.imgur.com/QdfnXvW.png";

        String body = "<html>"
                + "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;'>"
                + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 5px; overflow: hidden;'>"
                + "<div style='background-color: #1f2a44; padding: 15px; text-align: center;'>"
                + "<img src='" + logoImageURL + "' alt='LorKingdom Logo' style='width: 100px; display: inline-block;'>"
                + "</div>"
                + "<div style='padding: 30px; text-align: center;'>"
                + "<p style='color: #333333; font-size: 14px; margin: 20px 0 10px 0;'>Kính gửi " + username + ",</p>"
                + "<p style='color: #666666; font-size: 14px; margin: 0 0 20px 0;'>Bạn đã yêu cầu mã xác minh để đăng ký tài khoản của mình tại LorKingdom. Vui lòng sử dụng mã xác minh bên dưới để hoàn tất quy trình đăng ký của bạn:</p>"
                + "<div style='margin: 20px 0;'>"
                + "<p style='color: #666666; font-size: 14px; margin: 0 0 5px 0;'>Mã xác minh của bạn:</p>"
                + "<h1 style='color: #000000; font-size: 36px; font-weight: bold; margin: 0;'>" + verificationCode + "</h1>"
                + "<p style='color: #666666; font-size: 12px; margin: 5px 0 0 0;'>Vui lòng nhập mã này vào trang đăng ký để xác nhận tài khoản của bạn. Lưu ý rằng mã xác minh này sẽ hết hạn sau 2 phút.</p>"
                + "</div>"
                + "<p style='color: #666666; font-size: 14px; margin: 20px 0 0 0;'>Nếu bạn không yêu cầu mã xác minh này, vui lòng bỏ qua email này.</p>"
                + "<br>"
                + "<p style='color: #333333; font-size: 14px; margin: 10px 0 0 0;'>Trân trọng,</p>"
                + "<p style='color: #333333; font-size: 14px; margin: 5px 0 0 0;'>LorKingdom Support Team</p>"
                + "<p style='color: #666666; font-size: 12px; margin: 5px 0 0 0;'>lorkingdom99@gmail.com | 09123456789</p>"
                + "<p style='color: #666666; font-size: 12px; margin: 5px 0 0 0;'>LorKingdom.com.vn</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            EmailUtility.sendEmail(email, subject, body);
            // Nếu dùng Ajax, có thể trả về JSON thay vì redirect
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"error\": \"Không thể gửi email xác minh\"}");
        }

        // response.sendRedirect("verifyCode.jsp");
    }
}
