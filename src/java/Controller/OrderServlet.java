package Controller;

import DAO.OrderDAO;
import DAO.CartDAO; // Import CartDAO
import Model.CartItems;
import Model.Order;
import Model.OrderDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private CartDAO cartDAO; // Khai báo CartDAO

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO(); // Khởi tạo OrderDAO
        cartDAO = new CartDAO();   // Khởi tạo CartDAO
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userID");

        if (userId == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập để thanh toán!");
            response.sendRedirect("login.jsp");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null) {
            session.setAttribute("errorMessage", "Phương thức thanh toán không được chọn!");
            response.sendRedirect("order.jsp");
            return;
        }

        List<CartItems> listCart = (List<CartItems>) session.getAttribute("listCart");
        Double totalMoney = (Double) session.getAttribute("totalMoney");
        Double discount = (Double) session.getAttribute("discount");
        if (discount == null) {
            discount = 0.0;
        }

        if (listCart == null || totalMoney == null) {
            session.setAttribute("errorMessage", "Dữ liệu giỏ hàng không hợp lệ!");
            response.sendRedirect("order.jsp");
            return;
        }

        double finalTotal = totalMoney - discount;

        // Lấy thông tin khách hàng từ form
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");

        // Xử lý các phương thức thanh toán
        if ("zalopay".equals(paymentMethod)) {
            response.sendRedirect("ZaloPayPaymentServlet?totalMoney=" + finalTotal);
        } else if ("vnpay".equals(paymentMethod)) {
            response.sendRedirect("VNPayPaymentServlet?totalMoney=" + finalTotal);
        } else if ("momo".equals(paymentMethod)) {
            response.sendRedirect("MoMoPaymentServlet?totalMoney=" + finalTotal);
        } else if ("cash".equals(paymentMethod)) {
            // Tạo đối tượng Order
            Order order = new Order();
            order.setAccountName(fullName); // Lưu tên khách hàng
            order.setPayMentMethodName("Tiền mặt khi nhận hàng");
            order.setShipingMethodName("Giao hàng tiêu chuẩn"); // Giả định phương thức giao hàng mặc định
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");
            order.setTotalAmount(finalTotal);
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // Tạo danh sách OrderDetail từ giỏ hàng
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItems item : listCart) {
                OrderDetail detail = new OrderDetail();
                detail.setProductID(item.getProduct().getProductID());
                detail.setProductName(item.getProduct().getName());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice((float) item.getPrice());
                detail.setDiscount((float) (discount > 0 ? (discount / totalMoney) * 100 : 0)); // Phân bổ discount
                orderDetails.add(detail);
            }
            order.setOrderDetails(orderDetails); // Gán danh sách OrderDetails vào Order

            // Lưu vào cơ sở dữ liệu
            boolean success = orderDAO.saveOrder(order, userId, address, phone, email, city, district, ward);
            if (success) {
                session.setAttribute("successMessage", "Đặt hàng thành công! Đơn hàng sẽ được giao sớm nhất.");
                // Lưu thông tin đơn hàng và địa chỉ giao hàng vào session để hiển thị trên trang orderCash-success.jsp
                session.setAttribute("order", order);
                session.setAttribute("address", address);
                session.setAttribute("city", city);
                session.setAttribute("district", district);
                session.setAttribute("ward", ward);
                session.setAttribute("phone", phone);
                session.setAttribute("email", email);

                // Xóa toàn bộ giỏ hàng từ cơ sở dữ liệu
                try {
                    cartDAO.removeAll(userId);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi khi xóa giỏ hàng!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                // Xóa giỏ hàng từ session
                session.removeAttribute("listCart");
                session.removeAttribute("totalMoney");
                session.removeAttribute("discount");
                response.sendRedirect("orderCash-success.jsp");
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi đặt hàng!");
                response.sendRedirect("order.jsp");
            }
        } else {
            session.setAttribute("errorMessage", "Phương thức thanh toán không hợp lệ!");
            response.sendRedirect("order.jsp");
        }
    }
}