package Controller;

import DAO.OrderDAO;
import DAO.CartDAO;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
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
        String provinceCode = request.getParameter("province");
        String districtCode = request.getParameter("district");
        String wardCode = request.getParameter("ward");

        // Lấy tên từ API
        String provinceName = getLocationName("p", provinceCode);
        String districtName = getLocationName("d", districtCode);
        String wardName = getLocationName("w", wardCode);

        // Tạo chuỗi địa chỉ đầy đủ, tránh dấu phẩy thừa
        StringBuilder fullAddress = new StringBuilder(address);
        if (!wardName.isEmpty() && !wardName.startsWith("Unknown_") && !wardName.startsWith("Error_")) {
            fullAddress.append(", ").append(wardName);
        }
        if (!districtName.isEmpty() && !districtName.startsWith("Unknown_") && !districtName.startsWith("Error_")) {
            fullAddress.append(", ").append(districtName);
        }
        if (!provinceName.isEmpty() && !provinceName.startsWith("Unknown_") && !provinceName.startsWith("Error_")) {
            fullAddress.append(", ").append(provinceName);
        }

        // Xử lý các phương thức thanh toán
        if ("zalopay".equals(paymentMethod)) {
            response.sendRedirect("ZaloPayPaymentServlet?totalMoney=" + finalTotal);
        } else if ("vnpay".equals(paymentMethod)) {
            response.sendRedirect("VNPayPaymentServlet?totalMoney=" + finalTotal);
        } else if ("momo".equals(paymentMethod)) {
            response.sendRedirect("MoMoPaymentServlet?totalMoney=" + finalTotal);
        } else if ("cash".equals(paymentMethod)) {
            Order order = new Order();
            order.setAccountName(fullName);
            order.setPayMentMethodName("Tiền mặt khi nhận hàng");
            order.setShipingMethodName("Giao hàng tiêu chuẩn");
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
                detail.setDiscount((float) (discount > 0 ? (discount / totalMoney) * 100 : 0));
                orderDetails.add(detail);
            }
            order.setOrderDetails(orderDetails);

            boolean success = orderDAO.saveOrder(order, userId, fullAddress.toString(), phone, email, 
                                               provinceCode, districtCode, wardCode);
            if (success) {
                session.setAttribute("successMessage", "Đặt hàng thành công! Đơn hàng sẽ được giao sớm nhất.");
                session.setAttribute("order", order);
                session.setAttribute("address", fullAddress.toString());
                session.setAttribute("provinceCode", provinceCode);
                session.setAttribute("districtCode", districtCode);
                session.setAttribute("wardCode", wardCode);
                session.setAttribute("phone", phone);
                session.setAttribute("email", email);

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

    private String getLocationName(String type, String code) {
        if (code == null || code.isEmpty()) {
            LOGGER.warning("Location code is null or empty for type: " + type);
            return "";
        }

        String apiUrl = "https://provinces.open-api.vn/api/" + type + "/" + code;
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response apiResponse = client.newCall(request).execute()) {
            if (!apiResponse.isSuccessful()) {
                LOGGER.warning("API call failed for " + type + "/" + code + ": " + apiResponse.code());
                return "Unknown_" + code;
            }

            String jsonData = apiResponse.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.has("name")) {
                return jsonObject.getString("name");
            } else {
                LOGGER.warning("No 'name' field in API response for " + type + "/" + code + ": " + jsonData);
                return "Unknown_" + code;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calling API for " + type + "/" + code, e);
            return "Error_" + code;
        }
    }
}