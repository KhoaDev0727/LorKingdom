package Controller;

import DAO.OrderDAO;
import DAO.CartDAO;
import DAO.ProductDAO;
import DAO.ShippingDAO;
import Model.CartItems;
import Model.Order;
import Model.OrderDetail;
import Model.Promotion;
import Model.Shipping;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VNPayReturnServlet extends HttpServlet {

    private static final String vnp_HashSecret = "VL2ZFM15UPSSC2KGEU3X80VG7O23A3XV";
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Lấy tất cả tham số trả về từ VNPAY
        Map<String, String> fields = new TreeMap<>();
        for (String key : request.getParameterMap().keySet()) {
            String value = request.getParameter(key);
            if (value != null && !value.isEmpty()) {
                fields.put(key, value);
            }
        }

        // Lấy checksum từ VNPAY
        String vnp_SecureHash = fields.remove("vnp_SecureHash");

        // Tạo chuỗi hash data để kiểm tra
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            hashData.append(entry.getKey()).append("=")
                   .append(java.net.URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
        }
        hashData.setLength(hashData.length() - 1); // Xóa "&" cuối cùng

        // Tạo checksum từ dữ liệu trả về
        String calculatedHash = hmacSHA512(vnp_HashSecret, hashData.toString());

        // Kiểm tra giao dịch
        String transactionStatus = fields.get("vnp_TransactionStatus");
        boolean isSuccess = "00".equals(transactionStatus) && calculatedHash.equalsIgnoreCase(vnp_SecureHash);

        if (isSuccess) {
            Integer userId = (Integer) session.getAttribute("userID");
            List<CartItems> listCart = (List<CartItems>) session.getAttribute("listCart");
            Double totalMoney = (Double) session.getAttribute("totalMoney");
            Double discount = (Double) session.getAttribute("discount");
            Promotion appliedVoucher = (Promotion) session.getAttribute("appliedVoucher");

            if (discount == null) discount = 0.0;
            double finalTotal = totalMoney - discount;

            // Lấy thông tin từ session
            String email = (String) session.getAttribute("email");
            String fullName = (String) session.getAttribute("fullName");
            String phone = (String) session.getAttribute("phone");
            String finalAddress = (String) session.getAttribute("address");
            String provinceCode = (String) session.getAttribute("provinceCode");
            String districtCode = (String) session.getAttribute("districtCode");
            String wardCode = (String) session.getAttribute("wardCode");
            String wardName = (String) session.getAttribute("wardName");
            String districtName = (String) session.getAttribute("districtName");
            String provinceName = (String) session.getAttribute("provinceName");

            ShippingDAO shippingDAO = new ShippingDAO();
            Shipping shippingMethod = null;
            String shippingMethodId = (String) session.getAttribute("shippingMethod");
            try {
                List<Shipping> shippingMethods = shippingDAO.getAllShippingMethods();
                shippingMethod = shippingMethods.stream()
                        .filter(m -> m.getShippingMethodID() == Integer.parseInt(shippingMethodId))
                        .findFirst()
                        .orElse(null);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Tạo và lưu đơn hàng
            Order order = new Order();
            order.setAccountName(fullName);
            order.setPayMentMethodName("Ví điện tử VNPAY");
            order.setShipingMethodName(shippingMethod != null ? shippingMethod.getMethodName() : "Unknown");
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Paid"); // Đã thanh toán
            order.setTotalAmount(finalTotal);
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItems item : listCart) {
                OrderDetail detail = new OrderDetail();
                detail.setProductID(item.getProduct().getProductID());
                detail.setProductName(item.getProduct().getName());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice((float) item.getPrice());

                float discountPercent = 0.0f;
                if (appliedVoucher != null && item.getProduct().getProductID() == appliedVoucher.getProductID()) {
                    discountPercent = appliedVoucher.getDiscountPercent().floatValue();
                }
                detail.setDiscount(discountPercent);

                orderDetails.add(detail);
            }
            order.setOrderDetails(orderDetails);

            boolean success = orderDAO.saveOrder(order, userId, finalAddress, phone, email,
                    provinceCode, districtCode, wardCode);
            if (success) {
                try {
                    for (CartItems item : listCart) {
                        int productId = item.getProduct().getProductID();
                        int quantity = item.getQuantity();
                        boolean updated = productDAO.updateProductQuantity(productId, quantity);
                        if (!updated) {
                            session.setAttribute("errorMessage", "Không đủ số lượng sản phẩm trong kho!");
                            response.sendRedirect("order.jsp");
                            return;
                        }
                    }
                    cartDAO.removeAll(userId);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật số lượng hoặc xóa giỏ hàng!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                session.setAttribute("successMessage", "Thanh toán VNPay thành công! Đơn hàng sẽ được giao sớm nhất.");
                session.setAttribute("order", order);
                session.setAttribute("address", finalAddress);
                session.setAttribute("phone", phone);
                session.setAttribute("email", email);
                session.setAttribute("wardName", wardName);
                session.setAttribute("districtName", districtName);
                session.setAttribute("provinceName", provinceName);

                // Xóa dữ liệu tạm
                session.removeAttribute("listCart");
                session.removeAttribute("totalMoney");
                session.removeAttribute("discount");
                session.removeAttribute("appliedVoucher");
                session.removeAttribute("validationErrors");
                session.removeAttribute("formDataJson");
                session.removeAttribute("pendingVNPayOrder");

                response.sendRedirect("orderVNPay-success.jsp");
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu đơn hàng!");
                response.sendRedirect("order.jsp");
            }
        } else {
            session.setAttribute("errorMessage", "Thanh toán VNPay không thành công!");
            response.sendRedirect("order.jsp");
        }
    }

    private String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            mac.init(new javax.crypto.spec.SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512"));
            byte[] hmac = mac.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hmac) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha512", e);
        }
    }
}