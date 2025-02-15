package Controller;

import DBConnect.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String resetToken = generateToken();
        long expiryTime = System.currentTimeMillis() + (10 * 60 * 1000); // Token hết hạn sau 10 phút
        String userName = null; // Biến để lưu tên tài khoản

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Account WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Lấy tên tài khoản từ kết quả truy vấn
                userName = rs.getString("AccountName");

                ps = conn.prepareStatement("UPDATE Account SET reset_token = ?, token_expiry = ? WHERE email = ?");
                ps.setString(1, resetToken);
                ps.setLong(2, expiryTime);
                ps.setString(3, email);
                ps.executeUpdate();

                sendResetEmail(email, resetToken, userName); // Gửi email với tên người dùng
                request.setAttribute("message", "A reset link has been sent to your email.");
            } else {
                request.setAttribute("message", "Email not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "An error occurred. Please try again.");
        }

        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
    }

    private void sendResetEmail(String email, String token, String userName) throws MessagingException, UnsupportedEncodingException {
        final String fromEmail = "lorkingdom99@gmail.com";
        final String password = "lhvqnjnvthlsitqg";
        String resetLink = "http://localhost:9090/LorKingdom/resetPassword.jsp?token=" + token;

        // Cấu hình SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Thiết lập session cho email
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Tạo đối tượng message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail, "Lor-Kingdom"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Password Reset Request");

        // Tạo nội dung email 
        String htmlContent = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }"
                + ".email-container { background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 0 auto; }"
                + ".email-header { text-align: center; font-size: 24px; color: #333333; margin-bottom: 20px; }"
                + ".email-content { font-size: 16px; color: #555555; line-height: 1.6; margin-bottom: 20px; }"
                + ".button { display: inline-block; background: linear-gradient(90deg, #ff7a00, #ff5f00); color: white; padding: 14px 40px; border-radius: 50px; text-decoration: none; font-weight: bold; text-align: center; margin-top: 20px; transition: background 0.3s ease, transform 0.2s ease; }"
                + ".button:hover { background: linear-gradient(90deg, #e85c00, #e14f00); transform: scale(1.05); }"
                + ".footer { text-align: center; font-size: 14px; color: #888888; margin-top: 30px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='email-container'>"
                + "<div class='email-header'>Password Reset Request</div>"
                + "<div class='email-content'>"
                + "<p>Dear " + userName + ",</p>" // Chèn tên người dùng vào email
                + "<p>We have received a request to reset your password for your account. If you requested this reset, please click the button below to proceed.</p>"
                + "<a href='" + resetLink + "' class='button'>Reset Your Password</a>"
                + "<p>If you did not request a password reset, please ignore this email.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>If you have any questions, feel free to contact our support team.</p>"
                + "<p>&copy; 2025 Lor-Kingdom. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        message.setContent(htmlContent, "text/html");
        Transport.send(message);
    }

}
