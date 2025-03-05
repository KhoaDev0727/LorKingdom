package Controller;

import DAO.NotificationDAO;
import Model.Notification;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotificationServlet extends HttpServlet {

    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Notification Servlet";
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listNotifications(request, response);
            } else {
                switch (action) {
                    case "add":
                        addNotification(request, response);
                        break;
                    case "listDeleted":
                        listDeletedNotifications(request, response);
                        break;
                    case "update":
                        updateNotification(request, response);
                        break;
                    case "delete": // Soft delete
                        softDeleteNotification(request, response);
                        break;
                    case "hardDelete": // Hard delete
                        hardDeleteNotification(request, response);
                        break;
                    case "restore":
                        restoreNotification(request, response);
                        break;
                    case "search":
                        searchNotifications(request, response);
                        break;
                    default:
                        listNotifications(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Error: " + e.getMessage());
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
        }
    }

    private void listNotifications(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Notification> notifications = notificationDAO.getAllNotifications();
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("NotificationManagement.jsp").forward(request, response);
    }

    private void listDeletedNotifications(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Notification> notifications = notificationDAO.getDeletedNotifications();
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("NotificationManagement.jsp").forward(request, response);
    }

    private void addNotification(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String type = request.getParameter("type");
        String accountIDStr = request.getParameter("accountID");
        Integer accountID = (accountIDStr != null && !accountIDStr.trim().isEmpty()) ? Integer.parseInt(accountIDStr) : null;

        // Validation
        if (title == null || title.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tiêu đề không được để trống.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        if (title.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Tiêu đề quá dài. Chỉ được phép có tối đa 255 ký tự.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        if (type == null || (!type.equals("System") && !type.equals("Promotional") && !type.equals("User"))) {
            request.getSession().setAttribute("errorMessage", "Invalid notification type.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        if (accountID != null && accountID <= 0) {
            request.getSession().setAttribute("errorMessage", "ID tài khoản phải là số dương nếu được cung cấp.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        System.out.println(content);
        
        notificationDAO.addNotification(title, content, type, accountID);
        request.getSession().setAttribute("successMessage", "Thông báo đã được thêm thành công.");
        response.sendRedirect("NotificationServlet?action=list&showSuccessModal=true");
    }

    private void updateNotification(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String type = request.getParameter("type");
            String status = request.getParameter("status");
            String accountIDStr = request.getParameter("accountID");
            Integer accountID = (accountIDStr != null && !accountIDStr.trim().isEmpty()) ? Integer.parseInt(accountIDStr) : null;

            // Validation
            if (title == null || title.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tiêu đề không được để trống.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }
            if (title.length() > 255) {
                request.getSession().setAttribute("errorMessage", "Tiêu đề quá dài. Chỉ được phép có tối đa 255 ký tự.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }
            if (type == null || (!type.equals("System") && !type.equals("Promotional") && !type.equals("User"))) {
                request.getSession().setAttribute("errorMessage", "Invalid notification type.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }
            if (status == null || (!status.equals("Read") && !status.equals("Unread"))) {
                request.getSession().setAttribute("errorMessage", "Trạng thái không hợp lệ. Phải là 'Đã đọc' hoặc 'Chưa đọc'.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }
            if (accountID != null && accountID <= 0) {
                request.getSession().setAttribute("errorMessage", "ID tài khoản phải là số dương nếu được cung cấp.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }

            notificationDAO.updateNotification(notificationID, title, content, type, status, accountID);
            request.getSession().setAttribute("successMessage", "Thông báo đã được cập nhật thành công.");
            response.sendRedirect("NotificationServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID thông báo hoặc ID tài khoản không hợp lệ.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
        }
    }

    private void softDeleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.softDeleteNotification(notificationID);
            request.getSession().setAttribute("successMessage", "Thông báo đã được chuyển vào thùng rác.");
            response.sendRedirect("NotificationServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID thông báo không hợp lệ.");
            response.sendRedirect("NotificationServlet?action=list");
        }
    }

    private void hardDeleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.hardDeleteNotification(notificationID);
            request.getSession().setAttribute("successMessage", "Thông báo đã bị xóa vĩnh viễn.");
            response.sendRedirect("NotificationServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID thông báo không hợp lệ.");
            response.sendRedirect("NotificationServlet?action=listDeleted");
        }
    }

    private void restoreNotification(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.restoreNotification(notificationID);
            request.getSession().setAttribute("successMessage", "Thông báo đã được khôi phục thành công.");
            response.sendRedirect("NotificationServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID thông báo không hợp lệ.");
            response.sendRedirect("NotificationServlet?action=listDeleted");
        }
    }

    private void searchNotifications(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        List<Notification> notifications = notificationDAO.searchNotifications(keyword != null ? keyword : "");
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("NotificationManagement.jsp").forward(request, response);
    }
}