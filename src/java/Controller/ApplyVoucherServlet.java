package Controller;

import DAO.PromotionDAO;
import Model.CartItems;
import Model.Promotion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ApplyVoucherServlet extends HttpServlet {

    private final PromotionDAO promotionDAO;

    public ApplyVoucherServlet() throws SQLException, ClassNotFoundException {
        this.promotionDAO = new PromotionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String voucherCode = request.getParameter("voucherCode");
        List<Promotion> availableVouchers = (List<Promotion>) session.getAttribute("availableVouchers");
        List<CartItems> listCart = (List<CartItems>) session.getAttribute("listCart");

        if (voucherCode == null || availableVouchers == null || listCart == null) {
            out.print("{\"success\": false, \"message\": \"Dữ liệu không hợp lệ!\"}");
            return;
        }

        Promotion appliedVoucher = null;
        for (Promotion voucher : availableVouchers) {
            if (voucher.getPromotionCode().equals(voucherCode)) {
                appliedVoucher = voucher;
                break;
            }
        }

        if (appliedVoucher == null) {
            out.print("{\"success\": false, \"message\": \"Mã giảm giá không hợp lệ!\"}");
            return;
        }

        // Tìm sản phẩm trong giỏ hàng phù hợp với voucher
        double discount = 0.0;
        int productId = appliedVoucher.getProductID();
        for (CartItems item : listCart) {
            if (item.getProduct().getProductID() == productId) {
                discount = item.getPrice() * item.getQuantity() * (appliedVoucher.getDiscountPercent() / 100);
                break;
            }
        }

        if (discount == 0.0) {
            out.print("{\"success\": false, \"message\": \"Không có sản phẩm nào trong giỏ hàng phù hợp với mã giảm giá này!\"}");
            return;
        }

        session.setAttribute("discount", discount);
        session.setAttribute("appliedVoucher", appliedVoucher); // Lưu voucher đã áp dụng để sử dụng sau

        out.print("{\"success\": true, \"discount\": " + discount + "}");
        out.flush();
    }
}