package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import DAO.NotificationDAO;
import Model.Notification;
import java.util.List;

public class MarkNotificationReadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NotificationDAO notificationDAO = new NotificationDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String notificationId = request.getParameter("id");
        HttpSession session = request.getSession();

        if (notificationId != null && !notificationId.isEmpty()) {
            try {
                // Gọi phương thức từ NotificationDAO để đánh dấu thông báo là đã đọc
                boolean updated = notificationDAO.markNotificationAsRead(Integer.parseInt(notificationId));

                if (updated) {
                    // Lấy AccountID từ session
                    Integer accountId = (Integer) session.getAttribute("userID");
                    int unreadCount = 0;
                    if (accountId != null) {
                        // Lấy danh sách thông báo chưa đọc
                        List<Notification> notifications = notificationDAO.getNotificationsByAccountID(accountId);
                        for (Notification n : notifications) {
                            if ("Unread".equalsIgnoreCase(n.getStatus())) {
                                unreadCount++;
                            }
                        }
                        // Cập nhật session
                        session.setAttribute("notificationCount", unreadCount);
                        session.setAttribute("userNotifications", notifications);
                    }

                    // Trả về JSON
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    if (!response.isCommitted()) {
                        response.getWriter().write(String.format("{\"status\": \"success\", \"unreadCount\": %d}", unreadCount));
                    }
                } else {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\": \"error\", \"message\": \"No notification found\"}");
                }
            } catch (Exception e) { // Bắt cả SQLException và ClassNotFoundException
                e.printStackTrace();
                response.setContentType("application/json");
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Database error\"}");
            }
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid notification ID\"}");
        }
    }
}