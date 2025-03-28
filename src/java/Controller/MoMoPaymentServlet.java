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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MoMoPaymentServlet extends HttpServlet {

    private static final String ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    private static final String PARTNER_CODE = "MOMOBKUN20180529";
    private static final String ACCESS_KEY = "klm05TvNBzhg7h7j";
    private static final String SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";
    private static final String REDIRECT_URL = "http://localhost:9090/LorKingdom/orderMomo-success.jsp";
    private static final String IPN_URL = "http://localhost:9090/LorKingdom/orderMomo-success.jsp";

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String amountParam = request.getParameter("totalMoney");

        // Nếu đây là lần đầu truy cập (khởi tạo thanh toán)
        if (amountParam != null) {
            int amount;
            try {
                amount = (int) Double.parseDouble(amountParam);
            } catch (NumberFormatException e) {
                response.getWriter().println("Lỗi: Số tiền không hợp lệ.");
                return;
            }

            String orderId = "ORDER" + System.currentTimeMillis();
            String requestId = "REQ" + System.currentTimeMillis();
            String orderInfo = "Thanh toán đơn hàng qua MoMo";
            String extraData = "";
            String requestType = "captureWallet";

            // Tạo chữ ký HMAC SHA256
            String rawHash = "accessKey=" + ACCESS_KEY
                    + "&amount=" + amount
                    + "&extraData=" + extraData
                    + "&ipnUrl=" + IPN_URL
                    + "&orderId=" + orderId
                    + "&orderInfo=" + orderInfo
                    + "&partnerCode=" + PARTNER_CODE
                    + "&redirectUrl=" + REDIRECT_URL
                    + "&requestId=" + requestId
                    + "&requestType=" + requestType;

            String signature = hmacSHA256(rawHash, SECRET_KEY);

            // Lưu thông tin đơn hàng tạm thời vào session
            Map<String, String> orderData = new HashMap<>();
            orderData.put("orderId", orderId);
            orderData.put("amount", String.valueOf(amount));
            session.setAttribute("pendingMoMoOrder", orderData);

            // Tạo JSON request gửi đến MoMo
            Map<String, Object> data = new HashMap<>();
            data.put("partnerCode", PARTNER_CODE);
            data.put("partnerName", "MOMO");
            data.put("requestId", requestId);
            data.put("amount", amount);
            data.put("orderId", orderId);
            data.put("orderInfo", orderInfo);
            data.put("redirectUrl", REDIRECT_URL);
            data.put("ipnUrl", IPN_URL);
            data.put("lang", "vi");
            data.put("extraData", extraData);
            data.put("requestType", requestType);
            data.put("signature", signature);

            Gson gson = new Gson();
            String jsonRequest = gson.toJson(data);

            HttpURLConnection conn = (HttpURLConnection) new URL(ENDPOINT).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    Map<String, Object> jsonResponse = gson.fromJson(responseBuilder.toString(), Map.class);
                    response.sendRedirect((String) jsonResponse.get("payUrl"));
                }
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    response.getWriter().println("Lỗi từ MoMo: " + errorResponse.toString());
                }
            }
        } 
        // Xử lý callback từ MoMo (khi người dùng thanh toán xong)
        else {
            String resultCode = request.getParameter("resultCode");
            String orderId = request.getParameter("orderId");

            if ("0".equals(resultCode)) { // Thanh toán thành công
                Integer userId = (Integer) session.getAttribute("userID");
                List<CartItems> listCart = (List<CartItems>) session.getAttribute("listCart");
                Double totalMoney = (Double) session.getAttribute("totalMoney");
                Double discount = (Double) session.getAttribute("discount");
                Promotion appliedVoucher = (Promotion) session.getAttribute("appliedVoucher");

                if (discount == null) discount = 0.0;
                double finalTotal = totalMoney - discount;

                // Lấy thông tin từ session hoặc form (nếu cần)
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
                order.setPayMentMethodName("Ví điện tử MOMO");
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

                    session.setAttribute("successMessage", "Thanh toán MoMo thành công! Đơn hàng sẽ được giao sớm nhất.");
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
                    session.removeAttribute("pendingMoMoOrder");

                    response.sendRedirect("orderMomo-success.jsp");
                } else {
                    session.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu đơn hàng!");
                    response.sendRedirect("order.jsp");
                }
            } else {
                session.setAttribute("errorMessage", "Thanh toán MoMo không thành công!");
                response.sendRedirect("order.jsp");
            }
        }
    }

    // Hàm tạo chữ ký HMAC SHA256
    private static String hmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký HMAC", e);
        }
    }
}