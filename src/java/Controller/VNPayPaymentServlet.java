package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class VNPayPaymentServlet extends HttpServlet {

    private static final String vnp_TmnCode = "F7363RA1";
    private static final String vnp_HashSecret = "VL2ZFM15UPSSC2KGEU3X80VG7O23A3XV";
    private static final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String vnp_ReturnUrl = "http://localhost:9090/LorKingdom/orderVNPay-success.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String totalMoneyStr = request.getParameter("totalMoney");

        if (totalMoneyStr == null || totalMoneyStr.isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        double totalMoney;
        try {
            totalMoney = Double.parseDouble(totalMoneyStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp");
            return;
        }

        // VNPay yêu cầu số tiền dạng nguyên (nhân 100 để bỏ phần thập phân)
        String amount = String.valueOf((int) (totalMoney * 100));

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toán đơn hàng với tổng tiền " + totalMoney + " VND";
        String orderType = "250000";
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // Lưu thông tin tạm thời vào session
        Map<String, String> orderData = new TreeMap<>();
        orderData.put("vnp_TxnRef", vnp_TxnRef);
        orderData.put("amount", String.valueOf(totalMoney));
        session.setAttribute("pendingVNPayOrder", orderData);

        // Tạo map chứa các tham số
        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Tạo chuỗi hash data và checksum
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            hashData.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
        }
        hashData.setLength(hashData.length() - 1); // Xóa "&" cuối cùng

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        // Tạo URL thanh toán
        StringBuilder paymentUrl = new StringBuilder(vnp_Url).append("?");
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            paymentUrl.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                      .append("=")
                      .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                      .append("&");
        }
        paymentUrl.setLength(paymentUrl.length() - 1); // Xóa "&" cuối cùng

        // Chuyển hướng đến URL thanh toán VNPAY
        response.sendRedirect(paymentUrl.toString());
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