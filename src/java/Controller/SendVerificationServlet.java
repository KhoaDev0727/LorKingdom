package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import jakarta.servlet.http.HttpSession;

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
        String logoImageURL = "https://i.imgur.com/BRMPjnk.png";

        String body = "<html>"
                + "<body style='font-family: Arial, sans-serif; text-align: center;'>"
                + "<div style='padding: 10px;'>"
                + "<img src='" + bannerImageURL + "' alt='LorKingdom Banner' style='width: 100%; max-width: 950px; border-bottom: 1px solid grey;'>"
                + "</div>"
                + "<div style='padding: 10px;'>"
                + "<img src='" + logoImageURL + "' alt='LorKingdom Logo' style='width: 50px;'>"
                + "</div>"
                + "<p>Kính gửi " + username + ",</p>"
                + "<p>Bạn đã yêu cầu mã xác minh để đăng ký tài khoản của mình tại LorKingdom. Vui lòng sử dụng mã xác minh bên dưới để hoàn tất quy trình đăng ký của bạn:</p>"
                + "<h2>Mã xác minh mới của bạn: " + verificationCode + "</h2>"
                + "<p>Vui lòng nhập mã này vào trang đăng ký để xác nhận tài khoản của bạn. Lưu ý rằng mã xác minh này sẽ hết hạn sau 2 phút.</p>"
                + "<p>Nếu bạn không yêu cầu mã xác minh này, vui lòng bỏ qua email này.</p>"
                + "<br>"
                + "<p>Trân trọng,</p>"
                + "<p>LorKingdom Support Team</p>"
                + "<p>lorkingdom99@gmail.com | 09123456789</p>"
                + "<p>LorKingdom.com.vn</p>"
                + "</body>"
                + "</html>";

        try {
            EmailUtility.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể gửi email xác minh. Vui lòng thử lại sau.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("verifyCode.jsp");
    }
}
