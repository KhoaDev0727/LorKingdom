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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class OrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\sÀ-ỹ]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\sÀ-ỹ,/]+$");

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
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
        Promotion appliedVoucher = (Promotion) session.getAttribute("appliedVoucher");

        if (listCart == null || totalMoney == null) {
            session.setAttribute("errorMessage", "Dữ liệu giỏ hàng không hợp lệ!");
            response.sendRedirect("order.jsp");
            return;
        }

        if (discount == null) {
            discount = 0.0;
        }

        double finalTotal = totalMoney - discount;

        // Lấy thông tin khách hàng từ form
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String provinceCode = request.getParameter("province");
        String districtCode = request.getParameter("district");
        String wardCode = request.getParameter("ward");
        String provinceName = request.getParameter("provinceName");
        String districtName = request.getParameter("districtName");
        String wardName = request.getParameter("wardName");

        // Backend validation
        Map<String, String> errors = validateInput(email, fullName, phone, address, provinceCode, districtCode, wardCode);
        if (!errors.isEmpty()) {
            session.setAttribute("validationErrors", errors);
            Map<String, String> formData = new HashMap<>();
            formData.put("email", email != null ? email : "");
            formData.put("fullName", fullName != null ? fullName : "");
            formData.put("phone", phone != null ? phone : "");
            formData.put("address", address != null ? address : "");
            formData.put("province", provinceCode != null ? provinceCode : "");
            formData.put("district", districtCode != null ? districtCode : "");
            formData.put("ward", wardCode != null ? wardCode : "");
            formData.put("provinceName", provinceName != null ? provinceName : "");
            formData.put("districtName", districtName != null ? districtName : "");
            formData.put("wardName", wardName != null ? wardName : "");
            session.setAttribute("formDataJson", new JSONObject(formData).toString());
            response.sendRedirect("order.jsp");
            return;
        }

        StringBuilder fullAddress = new StringBuilder(address.trim());
        if (wardName != null && !wardName.isEmpty()) {
            fullAddress.append(", ").append(wardName);
        }
        if (districtName != null && !districtName.isEmpty()) {
            fullAddress.append(", ").append(districtName);
        }
        if (provinceName != null && !provinceName.isEmpty()) {
            fullAddress.append(", ").append(provinceName);
        }
        String finalAddress = fullAddress.toString().replaceAll(",\\s*$", "");

        String shippingMethodId = request.getParameter("shippingMethod");
        ShippingDAO shippingDAO = new ShippingDAO();
        Shipping shippingMethod = null;
        try {
            List<Shipping> shippingMethods = shippingDAO.getAllShippingMethods();
            shippingMethod = shippingMethods.stream()
                    .filter(m -> m.getShippingMethodID() == Integer.parseInt(shippingMethodId))
                    .findFirst()
                    .orElse(null);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Xử lý các phương thức thanh toán
        if ("vnpay".equals(paymentMethod)) {
            Order order = new Order();
            order.setAccountName(fullName);
            order.setPayMentMethodName("Ví điện tử VNPAY");
            order.setShipingMethodName(shippingMethod != null ? shippingMethod.getMethodName() : "Unknown");
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");
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
                // Trừ số lượng sản phẩm trong bảng Product
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
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật số lượng sản phẩm!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                // Xóa giỏ hàng
                try {
                    cartDAO.removeAll(userId);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi khi xóa giỏ hàng!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                // Lưu thông tin vào session trước khi chuyển hướng
                session.setAttribute("pendingOrderId", order.getOrderId());
                session.setAttribute("email", email);
                session.setAttribute("fullName", fullName);
                session.setAttribute("phone", phone);
                session.setAttribute("address", finalAddress);
                session.setAttribute("provinceCode", provinceCode);
                session.setAttribute("districtCode", districtCode);
                session.setAttribute("wardCode", wardCode);
                session.setAttribute("wardName", wardName);
                session.setAttribute("districtName", districtName);
                session.setAttribute("provinceName", provinceName);
                session.setAttribute("shippingMethod", shippingMethodId);

                session.removeAttribute("listCart");
                session.removeAttribute("totalMoney");
                session.removeAttribute("discount");
                session.removeAttribute("appliedVoucher");
                session.removeAttribute("validationErrors");
                session.removeAttribute("formDataJson");

                response.sendRedirect("VNPayPaymentServlet?totalMoney=" + finalTotal + "&orderId=" + order.getOrderId());
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu đơn hàng!");
                response.sendRedirect("order.jsp");
            }
        } else if ("momo".equals(paymentMethod)) {
            Order order = new Order();
            order.setAccountName(fullName);
            order.setPayMentMethodName("Ví điện tử MoMo"); // Tên phương thức thanh toán
            order.setShipingMethodName(shippingMethod != null ? shippingMethod.getMethodName() : "Unknown");
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending"); // Trạng thái ban đầu là Pending
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

            // Lưu đơn hàng vào cơ sở dữ liệu
            boolean success = orderDAO.saveOrder(order, userId, finalAddress, phone, email,
                    provinceCode, districtCode, wardCode);
            if (success) {
                // Trừ số lượng sản phẩm trong bảng Product
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
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật số lượng sản phẩm!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                // Xóa giỏ hàng
                try {
                    cartDAO.removeAll(userId);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi khi xóa giỏ hàng!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                // Lưu thông tin vào session trước khi chuyển hướng
                session.setAttribute("pendingOrderId", order.getOrderId());
                session.setAttribute("email", email);
                session.setAttribute("fullName", fullName);
                session.setAttribute("phone", phone);
                session.setAttribute("address", finalAddress);
                session.setAttribute("provinceCode", provinceCode);
                session.setAttribute("districtCode", districtCode);
                session.setAttribute("wardCode", wardCode);
                session.setAttribute("wardName", wardName);
                session.setAttribute("districtName", districtName);
                session.setAttribute("provinceName", provinceName);
                session.setAttribute("shippingMethod", shippingMethodId);

                // Xóa dữ liệu không cần thiết khỏi session
                session.removeAttribute("listCart");
                session.removeAttribute("totalMoney");
                session.removeAttribute("discount");
                session.removeAttribute("appliedVoucher");
                session.removeAttribute("validationErrors");
                session.removeAttribute("formDataJson");

                // Chuyển hướng đến MoMoPaymentServlet với tổng tiền và orderId
                response.sendRedirect("MoMoPaymentServlet?totalMoney=" + finalTotal + "&orderId=" + order.getOrderId());
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu đơn hàng!");
                response.sendRedirect("order.jsp");
            }
        } else if ("cash".equals(paymentMethod)) {
            Order order = new Order();
            order.setAccountName(fullName);
            order.setPayMentMethodName("Tiền mặt khi nhận hàng");
            order.setShipingMethodName(shippingMethod != null ? shippingMethod.getMethodName() : "Unknown");
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");
            order.setTotalAmount(finalTotal);
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItems item : listCart) {
                OrderDetail detail = new OrderDetail();
                detail.setProductID(item.getProduct().getProductID());
                detail.setProductName(item.getProduct().getName());
                detail.setProductImage(item.getProduct().getMainImageUrl());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice((float) item.getPrice());

                // Áp dụng giảm giá chỉ cho sản phẩm phù hợp với voucher
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
                // Trừ số lượng sản phẩm trong bảng Product
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
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật số lượng sản phẩm!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                session.setAttribute("successMessage", "Đặt hàng thành công! Đơn hàng sẽ được giao sớm nhất.");
                session.setAttribute("order", order);
                session.setAttribute("address", finalAddress);
                session.setAttribute("phone", phone);
                session.setAttribute("email", email);
                session.setAttribute("wardName", wardName);
                session.setAttribute("districtName", districtName);
                session.setAttribute("provinceName", provinceName);

                try {
                    cartDAO.removeAll(userId);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("errorMessage", "Có lỗi khi xóa giỏ hàng!");
                    response.sendRedirect("order.jsp");
                    return;
                }

                session.removeAttribute("listCart");
                session.removeAttribute("totalMoney");
                session.removeAttribute("discount");
                session.removeAttribute("appliedVoucher");
                session.removeAttribute("validationErrors");
                session.removeAttribute("formDataJson");
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

    private Map<String, String> validateInput(String email, String fullName, String phone,
            String address, String provinceCode, String districtCode, String wardCode) {
        Map<String, String> errors = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email không được để trống!");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            errors.put("fullName", "Họ và tên không được để trống!");
        } else if (!NAME_PATTERN.matcher(fullName).matches()) {
            errors.put("fullName", "Họ và tên không được chứa kí tự đặc biệt hoặc số!");
        }

        if (phone == null || phone.trim().isEmpty()) {
            errors.put("phone", "Số điện thoại không được để trống!");
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            errors.put("phone", "Số điện thoại chỉ được chứa số, dài 10 chữ số!");
        }

        if (address == null || address.trim().isEmpty()) {
            errors.put("address", "Địa chỉ không được để trống!");
        } else if (!ADDRESS_PATTERN.matcher(address).matches()) {
            errors.put("address", "Địa chỉ chỉ được chứa chữ, số, dấu cách, dấu phẩy hoặc dấu gạch chéo!");
        }

        if (provinceCode == null || provinceCode.trim().isEmpty()) {
            errors.put("province", "Vui lòng chọn tỉnh/thành phố!");
        }
        if (districtCode == null || districtCode.trim().isEmpty()) {
            errors.put("district", "Vui lòng chọn quận/huyện!");
        }
        if (wardCode == null || wardCode.trim().isEmpty()) {
            errors.put("ward", "Vui lòng chọn phường/xã!");
        }

        return errors;
    }
}
