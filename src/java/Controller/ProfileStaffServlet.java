package Controller;

import DAO.AccountDAO;
import Model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.json.JSONObject;

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

        account.setPassword("****"); // Placeholder thay cho hash
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
        Account dbAccount = accountDAO.getAccountByEmail(account.getEmail());

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String hashedPassword = MyUtils.hashPassword(newPassword);
            account.setPassword(hashedPassword);
        } else {
            account.setPassword(dbAccount.getPassword()); // Giữ nguyên hash cũ
        }

        account.setPhoneNumber(newPhoneNumber);
        account.setAddress(newAddress);

        boolean isUpdated = accountDAO.updateProfileStaffs(account.getEmail(), newAddress, newPhoneNumber, account.getPassword());

        // Trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();

        if (isUpdated) {
            account = accountDAO.getAccountByEmail(account.getEmail());
            account.setPassword("****"); // Placeholder cho client
            session.setAttribute("account", account);
            json.put("success", true);
            json.put("message", "Cập nhật thông tin nhân viên thành công!");
        } else {
            json.put("success", false);
            json.put("message", "Cập nhật thất bại, vui lòng thử lại.");
        }

        out.print(json.toString());
        out.flush();
    }
}