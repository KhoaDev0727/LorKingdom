package Controller;

import DAO.AccountDAO;
import Model.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class ProfileStaffServlet extends HttpServlet {

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

        if (account == null || account.getRoleID() != 2) { // Kiểm tra nếu không phải staff (RoleID = 2)
            response.sendRedirect("login.jsp");
            return;
        }

        // Đặt mật khẩu hiển thị là "****" thay vì hash thật
        account.setPassword("****");
        session.setAttribute("account", account);
        request.setAttribute("account", account);
        request.getRequestDispatcher("profileStaff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null || account.getRoleID() != 2) { // Kiểm tra role staff
            response.sendRedirect("login.jsp");
            return;
        }

        String newPhoneNumber = request.getParameter("phoneNumber");
        String newAddress = request.getParameter("address");
        String newPassword = request.getParameter("password");

        AccountDAO accountDAO = new AccountDAO();
        // Lấy thông tin tài khoản từ DB để giữ nguyên các giá trị không thay đổi
        Account dbAccount = accountDAO.getAccountByEmail(account.getEmail());

        // Nếu người dùng nhập mật khẩu mới, hash và cập nhật
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String hashedPassword = MyUtils.hashPassword(newPassword);
            account.setPassword(hashedPassword);
        } else {
            // Giữ nguyên mật khẩu hash cũ từ DB nếu không thay đổi
            account.setPassword(dbAccount.getPassword());
        }

        account.setPhoneNumber(newPhoneNumber);
        account.setAddress(newAddress);

        boolean isUpdated = accountDAO.updateProfileStaffs(account.getEmail(), newAddress, newPhoneNumber, account.getPassword());

        if (isUpdated) {
            // Cập nhật lại tài khoản từ DB
            account = accountDAO.getAccountByEmail(account.getEmail());
            // Đặt lại mật khẩu hiển thị là "****"
            account.setPassword("****");
            session.setAttribute("account", account);
        }

        request.getRequestDispatcher("profileStaff.jsp").forward(request, response);
    }
}