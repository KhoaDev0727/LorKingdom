package Controller;

import DAO.CartDAO;
import Model.CartItems;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class OrderServlet extends HttpServlet {

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

        if ("zalopay".equals(paymentMethod)) {
            response.sendRedirect("ZaloPayPaymentServlet?totalMoney=" + finalTotal);
        } else if ("vnpay".equals(paymentMethod)) {
            response.sendRedirect("VNPayPaymentServlet?totalMoney=" + finalTotal);
        } else if ("momo".equals(paymentMethod)) {
            response.sendRedirect("MoMoPaymentServlet?totalMoney=" + finalTotal);
        } else {
            session.setAttribute("errorMessage", "Phương thức thanh toán không hợp lệ!");
            response.sendRedirect("order.jsp");
        }
    }

}
