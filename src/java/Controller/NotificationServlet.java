package Controller;

import DAO.AccountDAO;
import org.jsoup.Jsoup;

import DAO.NotificationDAO;
import Model.Account;
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
                    case "sendToAll": // Gửi cho tất cả khách hàng RoleID = 3
                        sendToAllCustomers(request, response);
                        break;
                    case "sendSpecific": // Gửi cho khách hàng được chọn
                        sendToSpecificCustomers(request, response);
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

    private void sendToAllCustomers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        int notificationID = Integer.parseInt(request.getParameter("notificationID"));
        AccountDAO accountDAO = new AccountDAO();
        List<Account> customers = accountDAO.getAllAccountCustomer(3, 1, Integer.MAX_VALUE); // Lấy tất cả khách hàng RoleID = 3

        for (Account customer : customers) {
            notificationDAO.updateNotificationAccountID(notificationID, customer.getAccountId());
        }

        response.setContentType("text/plain");
        response.getWriter().write("Thông báo đã được gửi đến tất cả khách hàng thành công!");
    }

    private void sendToSpecificCustomers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException, ServletException {
        int notificationID = Integer.parseInt(request.getParameter("notificationID"));
        String[] accountIDs = request.getParameterValues("accountIDs");

        if (accountIDs != null && accountIDs.length > 0) {
            for (String accountIDStr : accountIDs) {
                int accountID = Integer.parseInt(accountIDStr);
                notificationDAO.updateNotificationAccountID(notificationID, accountID);
            }
            request.getSession().setAttribute("successMessage", "Thông báo đã được gửi đến khách hàng đã chọn thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Không có khách hàng nào được chọn!");
        }
        response.sendRedirect("NotificationServlet?action=list");
    }

    private void listNotifications(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalNotifications = notificationDAO.getTotalNotificationsCount();
        int totalPages = (int) Math.ceil((double) totalNotifications / pageSize);

        List<Notification> notifications = notificationDAO.getNotificationsByPage(page, pageSize);
        AccountDAO accountDAO = new AccountDAO();
        List<Account> accounts = accountDAO.getAllAccountCustomer(3, 1, Integer.MAX_VALUE); // Lấy tất cả khách hàng RoleID = 3

        request.setAttribute("notifications", notifications);
        request.setAttribute("accounts", accounts); // Truyền danh sách khách hàng
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("forward", "NotificationServlet?action=list");
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
        String plainTextContent = Jsoup.parse(content).text();
        if (plainTextContent == null || plainTextContent.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Nội dung không được để trống.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        String plainTextTitle = Jsoup.parse(title).text();
        if (plainTextTitle == null || plainTextTitle.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tiêu đề không được để trống.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        // ======= Validation =======
        // 1) Tiêu đề không được để trống
        if (title == null || title.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tiêu đề không được để trống.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        // 2) Tiêu đề không quá 255 ký tự
        if (title.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Tiêu đề quá dài. Chỉ được phép có tối đa 255 ký tự.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        // 3) Content không được để trống
        if (content == null || content.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Nội dung không được để trống.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        // 4) Type phải thuộc 1 trong 3 giá trị hợp lệ
        if (type == null || (!type.equals("System") && !type.equals("Promotional") && !type.equals("User"))) {
            request.getSession().setAttribute("errorMessage", "Loại thông báo không hợp lệ.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        // 5) accountID phải là số dương nếu có
        if (accountID != null && accountID <= 0) {
            request.getSession().setAttribute("errorMessage", "ID tài khoản phải là số dương nếu được cung cấp.");
            response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
            return;
        }
        // ======= End Validation =======

        // Thêm notification
        notificationDAO.addNotification(title, content, type, accountID);

        // Thông báo thành công
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
            String plainTextTitle = Jsoup.parse(title).text();
            if (plainTextTitle == null || plainTextTitle.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tiêu đề không được để trống.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }
            String plainTextContent = Jsoup.parse(content).text();
            if (plainTextContent == null || plainTextContent.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Nội dung không được để trống.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }

            // 2) Tiêu đề không quá 255 ký tự
            if (title.length() > 255) {
                request.getSession().setAttribute("errorMessage", "Tiêu đề quá dài. Chỉ được phép có tối đa 255 ký tự.");
                response.sendRedirect("NotificationServlet?action=list&showErrorModal=true");
                return;
            }

            // 4) Type phải hợp lệ
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

            // Thông báo thành công
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
