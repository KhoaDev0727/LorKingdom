package Controller;

import DAO.AccountDAO;
import Model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.json.JSONObject;

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

        // Lấy dữ liệu từ form
        String newUserName = request.getParameter("username");
        String newPhoneNumber = request.getParameter("phoneNumber");
        String newAddress = request.getParameter("address");
        String newPassword = request.getParameter("password");

        AccountDAO accountDAO = new AccountDAO();

        // Thiết lập response trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();

        // Kiểm tra độ dài mật khẩu nếu có mật khẩu mới
        if (newPassword != null && !newPassword.isEmpty()) {
            if (newPassword.length() < 6) {
                json.put("success", false);
                json.put("message", "Mật khẩu phải có ít nhất 6 ký tự!");
                out.print(json.toString());
                out.flush();
                return;
            }
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
            // Cập nhật lại thông tin tài khoản trong session
            account = accountDAO.getAccountByEmail(account.getEmail());
            account.setPassword("****"); // Placeholder cho client
            session.setAttribute("account", account);

            json.put("success", true);
            json.put("message", "Cập nhật thông tin thành công!");
        } else {
            json.put("success", false);
            json.put("message", "Cập nhật thất bại, vui lòng thử lại.");
        }

        out.print(json.toString());
        out.flush();
    }
}
