package Controller;

import DAO.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class VerifyCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enteredCode = request.getParameter("code");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("verificationCode") == null || session.getAttribute("codeExpiryTime") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String actualCode = (String) session.getAttribute("verificationCode");
        Long expiryTime = (Long) session.getAttribute("codeExpiryTime");
        long currentTime = System.currentTimeMillis();

        if (currentTime > expiryTime) {
            // Mã đã hết hạn
            session.removeAttribute("verificationCode");
            session.removeAttribute("codeExpiryTime");
            request.setAttribute("errorMessage", "Mã xác minh đã hết hạn. Vui lòng gửi lại mã mới.");
            request.getRequestDispatcher("verifyCode.jsp").forward(request, response);
            return;
        }

        if (enteredCode != null && enteredCode.equals(actualCode)) {
            // Xác thực thành công, loại bỏ mã xác minh khỏi session
            session.removeAttribute("verificationCode");
            session.removeAttribute("codeExpiryTime");
            session.setAttribute("isVerified", true);

            // Retrieve user information from session
            String username = (String) session.getAttribute("tempUsername");
            String email = (String) session.getAttribute("tempEmail");
            String password = (String) session.getAttribute("tempPassword");
            String phoneNumber = (String) session.getAttribute("tempPhoneNumber");

            if (username != null && email != null && password != null && phoneNumber != null) {
                // Hash the password before sending it to DAO
                String hashedPassword = MyUtils.hashPassword(password);
                saveUserToDatabase(username, email, hashedPassword, phoneNumber, session);

                // Clear session attributes after successful account creation
                session.removeAttribute("tempUsername");
                session.removeAttribute("tempEmail");
                session.removeAttribute("tempPassword");
                session.removeAttribute("tempPhoneNumber");
            }

            response.sendRedirect("verifyCode.jsp?status=success");
        } else {
            // Xác thực thất bại
            request.setAttribute("errorMessage", "Mã xác minh không đúng hoặc trống. Vui lòng thử lại.");
            request.getRequestDispatcher("verifyCode.jsp").forward(request, response);
        }
    }

    private void saveUserToDatabase(String username, String email, String password, String phoneNumber, HttpSession session) {
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.insertNewAccount(username, email, password, phoneNumber);
    }
}