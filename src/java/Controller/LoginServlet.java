package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import DAO.AccountDAO;
import DAO.CartDAO;
import DAO.NotificationDAO;
import Model.Account;
import Model.CartItems;
import Model.Notification;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {

    private CartDAO cartDAO = new CartDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
//        List<CartItems> ListCart = new ArrayList<>();
        validateInput(request, email, password);

        if (request.getAttribute("emailError") != null || request.getAttribute("passwordError") != null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.authenticateUser(email, password);
        if (account != null) {
            HttpSession session = request.getSession();
//            try {
//                ListCart = cartDAO.getCartItems(account.getAccountId());
            session.setAttribute("user", account.getUserName());
            session.setAttribute("email", account.getEmail());
            session.setAttribute("userID", account.getAccountId());
            session.setAttribute("userStatus", account.getStatus());
            session.setAttribute("roleID", account.getRoleID());
            session.setAttribute("account", account);
            session.setAttribute("imgePath", account.getImage());
//                session.setAttribute("totalCart", ListCart.size());

            // Kiểm tra thông báo cho AccountID
            try {
                List<Notification> userNotifications = notificationDAO.getNotificationsByAccountID(account.getAccountId());
                int unreadCount = 0;
                for (Notification n : userNotifications) {
                    if ("Unread".equalsIgnoreCase(n.getStatus())) {
                        unreadCount++;
                    }
                }
                session.setAttribute("userNotifications", userNotifications);
                session.setAttribute("notificationCount", unreadCount); // Chỉ đếm thông báo chưa đọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (account.getRoleID() == 3) {
                response.sendRedirect("home.jsp");
            } else {
                request.setAttribute("error", "Bạn không có quyền đăng nhập!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
//            } catch (SQLException | ClassNotFoundException ex) {
//                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không hợp lệ hoặc bạn không có quyền đăng nhập!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void validateInput(HttpServletRequest request, String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("emailError", "Email không được để trống!");
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            request.setAttribute("emailError", "Định dạng email không hợp lệ!");
        }
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "Mật khẩu không được để trống!");
        } else if (password.length() < 6) {
            request.setAttribute("passwordError", "Mật khẩu phải dài ít nhất 6 ký tự!");
        }
    }
}
