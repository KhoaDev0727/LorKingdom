package Controller;

import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import Model.Order;
import Model.OrderDetail;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageOrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ManageOrderServlet.class.getName());
    private static final int DEFAULT_PAGE = 1;
    private static final int ORDERS_PER_PAGE = 10;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Pagination handling
            String indexString = request.getParameter("index");
            int index = parsePageIndex(indexString);

            OrderDAO dao = new OrderDAO();
            int totalOrders = dao.getTotalOrders();
            int endPage = calculateEndPage(totalOrders, ORDERS_PER_PAGE);

            List<Order> listO = dao.getOrdersByPage(index, ORDERS_PER_PAGE);

            request.setAttribute("listO", listO);
            request.setAttribute("endPage", endPage);
            request.setAttribute("currentPage", index);

            request.getRequestDispatcher("OrderManagement.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid page number format", e);
            request.getSession().setAttribute("errorModal", "Số trang không hợp lệ");
            response.sendRedirect("OrderServlet?index=1");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing order management request", e);
            request.getSession().setAttribute("errorModal", "Lỗi hệ thống khi xử lý yêu cầu");
            response.sendRedirect("OrderServlet?index=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            request.getSession().setAttribute("errorModal", "Thiếu thông tin hành động");
            response.sendRedirect("OrderServlet");
            return;
        }

        try {
            switch (action) {
                case "delete":
                    handleDeleteOrder(request, response);
                    break;
                case "search":
                    handleSearchOrder(request, response);
                    break;
                case "sort":
                    handleSortOrder(request, response);
                    break;
                case "updateStatus":
                    handleUpdateStatus(request, response);
                    break;
                default:
                    request.getSession().setAttribute("errorModal", "Hành động không hợp lệ");
                    response.sendRedirect("OrderServlet");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing order action: " + action, e);
            request.getSession().setAttribute("errorModal", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("OrderServlet");
        }
    }

    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            request.getSession().setAttribute("errorModal", "Thiếu mã đơn hàng");
            response.sendRedirect("OrderServlet");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO dao = new OrderDAO();
            Order order = dao.getOrderByDelete(orderId);

            if (order == null) {
                request.getSession().setAttribute("errorModal", "Không tìm thấy đơn hàng");
                response.sendRedirect("OrderServlet");
                return;
            }

            if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
                dao.deleteOrder(orderId);
                request.getSession().setAttribute("successModal", "Xóa đơn hàng thành công.");
            } else {
                request.getSession().setAttribute("errorModal",
                        "Chỉ có thể xóa đơn hàng ở trạng thái ' Đã Hủy'");
            }
            response.sendRedirect("OrderServlet");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorModal", "Mã đơn hàng không hợp lệ");
            response.sendRedirect("OrderServlet");
        }
    }

    private void handleSearchOrder(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String customerName = request.getParameter("customerName");

        if (customerName == null || customerName.trim().isEmpty()) {
            request.getSession().setAttribute("errorModal", "Vui lòng nhập tên khách hàng để tìm kiếm");
            response.sendRedirect("OrderServlet");
            return;
        }

        if (customerName.length() > 100) {
            request.getSession().setAttribute("errorModal", "Tên khách hàng quá dài");
            response.sendRedirect("OrderServlet");
            return;
        }

        OrderDAO dao = new OrderDAO();
        List<Order> listO = dao.searchByCustomerName(customerName.trim());

        if (listO.isEmpty()) {
            request.setAttribute("searchMessage", "Không tìm thấy đơn hàng nào cho '" + customerName + "'");
        }

        request.setAttribute("listO", listO);
        request.setAttribute("searchedName", customerName);
        request.getRequestDispatcher("OrderManagement.jsp").forward(request, response);
    }

    private void handleSortOrder(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String sortOrder = request.getParameter("sortOrder");

        // Default to ASC if invalid value
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC";
        }

        OrderDAO dao = new OrderDAO();
        List<Order> list = dao.sort(sortOrder);

        request.setAttribute("listO", list);
        request.setAttribute("currentSort", sortOrder);
        request.getRequestDispatcher("OrderManagement.jsp").forward(request, response);
    }

    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");

        // Validate required parameters
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorModal", "Thiếu mã đơn hàng");
            response.sendRedirect("OrderServlet");
            return;
        }

        if (status == null || status.trim().isEmpty()) {
            request.getSession().setAttribute("errorModal", "Thiếu trạng thái cập nhật");
            response.sendRedirect("OrderServlet");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO dao = new OrderDAO();

            // First get the current order to validate
            Order currentOrder = dao.getOrderByIdUpdate(orderId);
            if (currentOrder == null) {
                request.getSession().setAttribute("errorModal", "Không tìm thấy đơn hàng với mã: " + orderId);
                response.sendRedirect("OrderServlet");
                return;
            }

            // Get status ID from database
            int statusId = dao.getStatusIdByName(status.trim());
            if (statusId == -1) {
                request.getSession().setAttribute("errorModal", "Trạng thái không hợp lệ: " + status);
                response.sendRedirect("OrderServlet");
                return;
            }

            // Validate status transition
            if (!isValidStatusTransition(currentOrder.getStatus(), status)) {
                String currentStatusVN = translateStatusToVietnamese(currentOrder.getStatus());
                String newStatusVN = translateStatusToVietnamese(status);
                request.getSession().setAttribute("errorModal",
                        "Không thể chuyển từ trạng thái " + currentStatusVN + " sang " + newStatusVN);
                response.sendRedirect("OrderServlet");
                return;
            }

            // Perform the update
            boolean success = dao.updateOrderStatus(orderId, statusId);
            if (success) {
                request.getSession().setAttribute("successModal", "Cập nhật trạng thái thành công");
            } else {
                request.getSession().setAttribute("errorModal", "Cập nhật trạng thái thất bại");
            }
            response.sendRedirect("OrderServlet");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorModal", "Mã đơn hàng phải là số");
            response.sendRedirect("OrderServlet");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order status", e);
            request.getSession().setAttribute("errorModal", "Lỗi hệ thống khi cập nhật trạng thái");
            response.sendRedirect("OrderServlet");
        }
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equalsIgnoreCase(newStatus)) {
            return true; // Allow staying in same status
        }

        switch (currentStatus.toLowerCase()) {
            case "pending":
                return newStatus.equalsIgnoreCase("Shipped")
                        || newStatus.equalsIgnoreCase("Cancelled");
            case "shipped":
                return newStatus.equalsIgnoreCase("Delivered");
            case "delivered":
            case "cancelled":
                return false; // No transitions allowed from these states
            default:
                return false;
        }
    }

    private String translateStatusToVietnamese(String englishStatus) {
        if (englishStatus == null) {
            return "Không xác định";
        }

        switch (englishStatus.toLowerCase()) {
            case "pending":
                return "Đang xác nhận";
            case "shipped":
                return "Chờ giao hàng";
            case "delivered":
                return "Đã nhận hàng";
            case "cancelled":
                return "Đã hủy";
            default:
                return englishStatus; // Trả về nguyên bản nếu không khớp
        }
    }

    private int parsePageIndex(String indexString) {
        if (indexString == null || indexString.isEmpty()) {
            return DEFAULT_PAGE;
        }
        try {
            return Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE;
        }
    }

    private int calculateEndPage(int totalItems, int itemsPerPage) {
        if (totalItems <= 0) {
            return 1;
        }
        return (totalItems + itemsPerPage - 1) / itemsPerPage;
    }

    @Override
    public String getServletInfo() {
        return "Order Management Servlet";
    }
}
