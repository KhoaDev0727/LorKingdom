package Controller;

import DAO.AccountDAO;
import Model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;

public class DeactivateAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        String inputPassword = request.getParameter("password");

        AccountDAO accountDAO = new AccountDAO();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();

        // Xác thực mật khẩu
        String hashedInputPassword = MyUtils.hashPassword(inputPassword);
        Account dbAccount = accountDAO.getAccountByEmail(account.getEmail());
        if (!dbAccount.getPassword().equals(hashedInputPassword)) {
            json.put("success", false);
            json.put("message", "Mật khẩu không chính xác!");
            out.print(json.toString());
            out.flush();
            return;
        }

        // Cập nhật trạng thái tài khoản thành "Ngừng hoạt động"
        boolean isDeactivated = accountDAO.deactivateAccount(account.getEmail());
        if (isDeactivated) {
            json.put("success", true);
            json.put("message", "Tài khoản của bạn đã được vô hiệu hóa thành công!");
            session.invalidate(); // Hủy session sau khi vô hiệu hóa
        } else {
            json.put("success", false);
            json.put("message", "Không thể vô hiệu hóa tài khoản, vui lòng thử lại!");
        }

        out.print(json.toString());
        out.flush();
    }
}