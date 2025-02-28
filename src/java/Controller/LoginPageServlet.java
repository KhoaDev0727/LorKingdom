package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import Model.Account;
import DAO.AccountDAO;

public class LoginPageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validation logic
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("emailError", "Email không được để trống!");
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            request.setAttribute("emailError", "Định dạng email không hợp lệ!");
        }
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "Mật khẩu không được để trống!");
        } else if (password.length() < 6) {
            request.setAttribute("passwordError", "Mật khẩu phải dài ít nhất 6 ký tự!");
        }

        if (request.getAttribute("emailError") != null || request.getAttribute("passwordError") != null) {
            request.getRequestDispatcher("loginPage.jsp").forward(request, response);
            return;
        }

        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.authenticateAdminStaff(email, password);

        if (account != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", account.getUserName());
            session.setAttribute("email", account.getEmail());
            session.setAttribute("userID", account.getAccountId());
            session.setAttribute("roleID", account.getRoleID());
            session.setAttribute("account", account);
            session.setMaxInactiveInterval(10000000); // 10 giây
            // Redirect based on role
            if (account.getRoleID() == 1) {
                response.sendRedirect("DashBoard.jsp"); // Admin dashboard
            } else if (account.getRoleID() == 2 || account.getRoleID() == 4 ) {
                response.sendRedirect("profileStaff.jsp"); // Staff dashboard
            } else {
                request.setAttribute("error", "Bạn không có quyền đăng nhập!");
                request.getRequestDispatcher("loginPage.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không hợp lệ!");
            request.getRequestDispatcher("loginPage.jsp").forward(request, response);
        }
        
    }
}
