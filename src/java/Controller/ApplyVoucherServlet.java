package Controller;

import DAO.PromotionDAO;
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
        Double totalMoney = (Double) session.getAttribute("totalMoney");

        if (voucherCode == null || availableVouchers == null || totalMoney == null) {
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

        double discountPercent = appliedVoucher.getDiscountPercent();
        double discount = totalMoney * (discountPercent / 100);
        session.setAttribute("discount", discount);

        out.print("{\"success\": true, \"discount\": " + discount + "}");
        out.flush();
    }
}