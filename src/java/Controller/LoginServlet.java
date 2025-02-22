package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import DAO.AccountDAO;
import Model.Account;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        validateInput(request, email, password);

        if (request.getAttribute("emailError") != null || request.getAttribute("passwordError") != null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Hash the password before checking
        String hashedPassword = MyUtils.hashPassword(password);

        Account account = new AccountDAO().authenticateUser(email, hashedPassword);

        if (account != null && account.getRoleID() == 3) { // Check if RoleID is 3 for customers
            HttpSession session = request.getSession();
            session.setAttribute("user", account.getUserName());
            session.setAttribute("email", account.getEmail());
            session.setAttribute("userID", account.getAccountId());
            session.setAttribute("userStatus", account.getStatus());
            session.setAttribute("account", account); // Store account in session

            response.sendRedirect("home.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password, or you do not have permission to log in!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void validateInput(HttpServletRequest request, String email, String password) {
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
    }
}