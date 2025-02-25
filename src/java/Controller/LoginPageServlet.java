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
            request.setAttribute("emailError", "Email cannot be empty!");
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            request.setAttribute("emailError", "Invalid email format!");
        }
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "Password cannot be empty!");
        } else if (password.length() < 6) {
            request.setAttribute("passwordError", "Password must be at least 6 characters long!");
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
            session.setMaxInactiveInterval(10); // 10 giây

            // Redirect based on role
            if (account.getRoleID() == 1) {
                response.sendRedirect("DashBoard.jsp"); // Admin dashboard
            } else if (account.getRoleID() == 2) {

                response.sendRedirect("staffDashboard.jsp"); // Staff dashboard
            } else {
                // Handle other roles or default case
                response.sendRedirect("home.jsp"); // Or wherever you want non-admin/staff to go
            }
        } else {
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("loginPage.jsp").forward(request, response);
        }
    }
}
