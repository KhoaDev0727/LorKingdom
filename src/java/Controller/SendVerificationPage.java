package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import jakarta.servlet.http.HttpSession;

public class SendVerificationPage extends HttpServlet {

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
            response.sendRedirect("registerPage.jsp"); 
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
        String bannerImageURL = "https://i.imgur.com/VS2VFkL.jpeg";
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
                + "<p>Chào mừng đến với đội ngũ LorKingdom! Chúng tôi rất vui mừng khi bạn trở thành thành viên mới của đội ngũ.</p>"
                + "<p>Để hoàn tất đăng ký và truy cập vào cổng thông tin nhân viên, vui lòng sử dụng mã xác minh bên dưới:</p>"
                + "<h2>Mã xác minh của bạn: " + verificationCode + "</h2>"
                + "<p>Mã này cần thiết để xác nhận danh tính của bạn và hoàn tất thiết lập tài khoản. Xin lưu ý rằng mã sẽ hết hạn sau 2 phút.</p>"
                + "<p>Nếu bạn không yêu cầu mã xác minh này, vui lòng liên hệ với chúng tôi ngay lập tức.</p>"
                + "<br>"
                + "<p>Trân trọng,</p>"
                + "<p>LorKingdom HR Team</p>"
                + "<p>lorkingdom99@gmail.com | 09123456789</p>"
                + "<p>LorKingdom.com.vn</p>"
                + "</body>"
                + "</html>";


        try {
            EmailUtility.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể gửi email xác minh. Vui lòng thử lại sau.");
            request.getRequestDispatcher("registerPage.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("verifyCodePage.jsp");
    }
}
