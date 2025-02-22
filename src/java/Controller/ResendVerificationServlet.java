/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;

public class ResendVerificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
   
    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin người dùng từ session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("tempEmail") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("tempEmail");
        String username = (String) session.getAttribute("tempUsername");
        String verificationCode = generateVerificationCode();

        // Tạo thời gian hết hạn (10 phút)
        long currentTime = System.currentTimeMillis();
        long expiryTime = currentTime + (2 * 60 * 1000);

        // Cập nhật mã xác minh và thời gian hết hạn vào session
        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("codeExpiryTime", expiryTime);

        String subject = "Register Verification Code - LorKingdom";
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
                + "<p>Dear " + username + ",</p>"
                + "<p>You requested a new verification code to register your account at LorKingdom. Please use the verification code below to complete your register process:</p>"
                + "<h2>Your new verification code: " + verificationCode + "</h2>"
                + "<p>Please enter this code on the register page to confirm your account. Note that this verification code will expire after 2 minutes.</p>"
                + "<p>If you did not request this verification code, please ignore this email.</p>"
                + "<br>"
                + "<p>Best regards,</p>"
                + "<p>LorKingdom Support Team</p>"
                + "<p>lorkingdom99@gmail.com | 09123456789</p>"
                + "<p>LorKingdom.com.vn</p>"
                + "</body>"
                + "</html>";

        
        try {
            EmailUtility.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "The verification email could not be resent. Please try again later.");
            request.getRequestDispatcher("verifyCode.jsp").forward(request, response);
            return;
        }

        // Thông báo gửi lại mã thành công
        request.setAttribute("infoMessage", "Verification code has been resent. Please check your email.");
        request.getRequestDispatcher("verifyCode.jsp").forward(request, response);
    }
}