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
import jakarta.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Account WHERE Email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Lấy tên tài khoản từ kết quả truy vấn
                userName = rs.getString("AccountName");

                ps = conn.prepareStatement("UPDATE Account SET ResetToken = ?, TokenExpiry = ? WHERE Email = ?");
                ps.setString(1, resetToken);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000));
                ps.setString(3, email);
                ps.executeUpdate();

                sendResetEmail(email, resetToken, userName); // Gửi email với tên người dùng
                request.setAttribute("message", "Một liên kết đặt lại đã được gửi đến email của bạn.");
            } else {
                request.setAttribute("message", "Không tìm thấy email.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Đã xảy ra lỗi. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
    }

    private void sendResetEmail(String email, String token, String userName) throws MessagingException, UnsupportedEncodingException {
        final String fromEmail = "lorkingdom99@gmail.com";
        final String password = "lhvqnjnvthlsitqg";
        String resetLink = "http://localhost:8080/LorKingdom/resetPassword.jsp?token=" + token;

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
        message.setSubject(MimeUtility.encodeText("Yêu cầu đặt lại mật khẩu", "UTF-8", "B"));

        // Tạo nội dung email 
        String htmlContent = "<html>"
        + "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;'>"
        + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 5px; overflow: hidden;'>"
        + "<div style='background-color: #1f2a44; padding: 15px; text-align: center;'>"
        + "<img src='https://i.imgur.com/QdfnXvW.png' alt='LorKingdom Logo' style='width: 100px; display: inline-block;'>"
        + "</div>"
        + "<div style='padding: 30px; text-align: center;'>"
        + "<h2 style='color: #333333; font-size: 20px; margin: 0 0 20px 0;'>Yêu cầu đặt lại mật khẩu</h2>"
        + "<p style='color: #333333; font-size: 14px; margin: 0 0 10px 0;'>Kính gửi " + userName + ",</p>"
        + "<p style='color: #666666; font-size: 14px; margin: 0 0 20px 0;'>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Nếu bạn yêu cầu đặt lại này, vui lòng nhấp vào nút bên dưới để tiếp tục.</p>"
        + "<a href='" + resetLink + "' style='display: inline-block; background-color: #1f2a44; color: #ffffff; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-size: 14px; font-weight: bold; margin: 20px 0;'>Đặt lại mật khẩu của bạn</a>"
        + "<p style='color: #666666; font-size: 14px; margin: 20px 0 0 0;'>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>"
        + "<p style='color: #666666; font-size: 12px; margin: 30px 0 0 0;'>Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với nhóm hỗ trợ của chúng tôi.</p>"
        + "<p style='color: #666666; font-size: 12px; margin: 5px 0 0 0;'>© 2025 Lor-Kingdom. Đã đăng ký bản quyền.</p>"
        + "</div>"
        + "</div>"
        + "</body>"
        + "</html>";

        message.setContent(htmlContent, "text/html; charset=UTF-8");
        Transport.send(message);
    }

}
