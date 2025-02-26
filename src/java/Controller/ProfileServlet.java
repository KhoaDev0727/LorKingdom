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

        // Không hiển thị mật khẩu hash, để mặc định là placeholder
        account.setPassword("****"); // Placeholder thay cho hash

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

        String newUserName = request.getParameter("username");
        String newPhoneNumber = request.getParameter("phoneNumber");
        String newAddress = request.getParameter("address");
        String newPassword = request.getParameter("password");

        AccountDAO accountDAO = new AccountDAO();
        // Nếu người dùng nhập mật khẩu mới, hash nó trước khi lưu vào database
        if (newPassword != null && !newPassword.isEmpty()) {
            String hashedPassword = MyUtils.hashPassword(newPassword);
            account.setPassword(hashedPassword); // Lưu hash để cập nhật vào database
        } else {
            account.setPassword(accountDAO.getAccountByEmail(account.getEmail()).getPassword()); // Giữ nguyên hash cũ
        }

        account.setUserName(newUserName);
        account.setPhoneNumber(newPhoneNumber);
        account.setAddress(newAddress);

        boolean isUpdated = accountDAO.updateProfileCustomer(account.getEmail(), newAddress, newPhoneNumber, account.getPassword());

        if (isUpdated) {
            // Lấy lại thông tin tài khoản từ database
            account = accountDAO.getAccountByEmail(account.getEmail());
            // Gán mật khẩu hiển thị là "****" thay vì chuỗi hash
            account.setPassword("****");
            session.setAttribute("account", account); // Cập nhật session với account mới
        }

        // Forward thay vì redirect để giữ trạng thái ngay lập tức
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}