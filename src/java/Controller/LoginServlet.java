package Controller;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import DBConnect.DBConnection;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

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
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Account WHERE Email = ? AND Password = ? AND IsDeleted = 0";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", rs.getString("UserName"));
                        response.sendRedirect("home.jsp");
                    } else {
                        request.setAttribute("error", "Invalid email or password!");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
