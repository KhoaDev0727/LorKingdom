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

        String subject = "Register Verification Code - LorKingdom";
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
                + "<p>Dear " + username + ",</p>"
                + "<p>Welcome to the LorKingdom team! We're excited to have you onboard as a new staff member.</p>"
                + "<p>To complete your registration and gain access to the staff portal, please use the verification code below:</p>"
                + "<h2>Your verification code: " + verificationCode + "</h2>"
                + "<p>This code is necessary to confirm your identity and finalize your account setup. Please note that the code will expire in 2 minutes.</p>"
                + "<p>If you did not request this verification code, please contact us immediately.</p>"
                + "<br>"
                + "<p>Best regards,</p>"
                + "<p>LorKingdom HR Team</p>"
                + "<p>lorkingdom99@gmail.com | 09123456789</p>"
                + "<p>LorKingdom.com.vn</p>"
                + "</body>"
                + "</html>";


        try {
            EmailUtility.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "The verification email could not be sent. Please try again later.");
            request.getRequestDispatcher("registerPage.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("verifyCodePage.jsp");
    }
}
