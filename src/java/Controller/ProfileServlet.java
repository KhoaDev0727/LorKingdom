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

        session.setAttribute("account", account); 
        
        request.setAttribute("account", account);
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy dữ liệu từ form
        String newUserName = request.getParameter("username");
        String newPhoneNumber = request.getParameter("phoneNumber");
        String newAddress = request.getParameter("address");
        String newPassword = request.getParameter("password");

        // Cập nhật dữ liệu
        account.setUserName(newUserName);
        account.setPhoneNumber(newPhoneNumber);
        account.setAddress(newAddress);
        account.setPassword(newPassword);

        AccountDAO accountDAO = new AccountDAO();
        boolean isUpdated = accountDAO.updateProfileCustomer(account.getEmail(), newAddress, newPhoneNumber, newPassword); // Gọi phương thức cập nhật

        if (isUpdated) {
            session.setAttribute("account", accountDAO.getAccountByEmail(account.getEmail())); // Cập nhật lại session với thông tin mới
        }

        response.sendRedirect("profile.jsp");
    }
}
