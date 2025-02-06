package Controller;

import DAO.AccountDAO;
import Model.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.getAccountByEmail(email);

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Truyền account vào request để hiển thị trong profile.jsp
        request.setAttribute("account", account);
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDAO accountDAO = new AccountDAO();

        // Lấy account từ session
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy dữ liệu từ form
        String newAddress = request.getParameter("address");
        String newPhoneNumber = request.getParameter("phoneNumber");

        // Cập nhật vào database
        accountDAO.updateAddress(account.getEmail(), newAddress);
        accountDAO.updatePhoneNumber(account.getEmail(), newPhoneNumber);

        // Load lại account từ database
        Account updatedAccount = accountDAO.getAccountByEmail(account.getEmail());
        if (updatedAccount != null) {
            session.setAttribute("account", updatedAccount); // Cập nhật lại session với dữ liệu mới
        }

        // Chuyển hướng lại trang profile
        response.sendRedirect("profile.jsp");
    }

}
