package Controller;

import DAO.CartDAO;
import DAO.PromotionDAO;
import Model.CartItems;
import Model.Promotion;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckOutServlet extends HttpServlet {

    private final CartDAO cartDAO;
    private final PromotionDAO promotionDAO;

    public CheckOutServlet() throws SQLException, ClassNotFoundException {
        this.cartDAO = new CartDAO();
        this.promotionDAO = new PromotionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userID");

            if (userId == null) {
                session.setAttribute("errorMessage", "Vui lòng đăng nhập để thanh toán!");
                response.sendRedirect("login.jsp");
                return;
            }

            List<CartItems> listCart = cartDAO.getCartItems(userId);
            if (listCart == null || listCart.isEmpty()) {
                session.setAttribute("errorMessage", "Giỏ hàng của bạn đang trống!");
                response.sendRedirect("cart.jsp");
                return;
            }

            double totalMoney = calculateTotalMoney(listCart);
            Double discount = (Double) session.getAttribute("discount");
            if (discount == null) {
                discount = 0.0;
            }

            // Lấy danh sách voucher áp dụng cho sản phẩm trong giỏ hàng
            List<Promotion> availableVouchers = getAvailableVouchers(listCart);
            session.setAttribute("availableVouchers", availableVouchers);

            session.setAttribute("listCart", listCart);
            session.setAttribute("totalMoney", totalMoney);
            session.setAttribute("size", listCart.size());
            session.setAttribute("discount", discount);

            request.setAttribute("listCart", listCart);
            request.setAttribute("totalMoney", totalMoney);
            request.setAttribute("size", listCart.size());
            request.setAttribute("discount", discount);
            request.setAttribute("availableVouchers", availableVouchers);

            request.getRequestDispatcher("order.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu khi tải giỏ hàng!");
            response.sendRedirect("cart.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý thanh toán!");
            response.sendRedirect("cart.jsp");
        }
    }

    private double calculateTotalMoney(List<CartItems> items) {
        double total = 0.0;
        for (CartItems item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private List<Promotion> getAvailableVouchers(List<CartItems> cartItems) throws SQLException, ClassNotFoundException {
        List<Promotion> vouchers = new ArrayList<>();
        for (CartItems item : cartItems) {
            int productId = item.getProduct().getProductID();
            List<Promotion> productVouchers = promotionDAO.getPromotionsByProductId(productId);
            if (productVouchers != null) {
                vouchers.addAll(productVouchers);
            }
        }
        return vouchers;
    }
}