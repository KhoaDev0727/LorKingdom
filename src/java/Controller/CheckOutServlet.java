package Controller;

import DAO.CartDAO;
import Model.CartItems;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CheckOutServlet extends HttpServlet {

    private final CartDAO cartDAO;

    public CheckOutServlet() throws SQLException, ClassNotFoundException {
        this.cartDAO = new CartDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy session
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userID"); 

            // Kiểm tra người dùng đã đăng nhập chưa
            if (userId == null) {
                session.setAttribute("errorMessage", "Vui lòng đăng nhập để thanh toán!");
                response.sendRedirect("login.jsp");
                return;
            }

            // Lấy danh sách sản phẩm trong giỏ hàng
            List<CartItems> listCart = cartDAO.getCartItems(userId);
            if (listCart == null || listCart.isEmpty()) {
                session.setAttribute("errorMessage", "Giỏ hàng của bạn đang trống!");
                response.sendRedirect("cart.jsp");
                return;
            }

            // Tính tổng tiền
            double totalMoney = calculateTotalMoney(listCart);

            // Giá trị discount (mặc định là 0, sẽ được cập nhật từ order.jsp nếu có mã giảm giá)
            Double discount = (Double) session.getAttribute("discount");
            if (discount == null) {
                discount = 0.0;
            }

            // Lưu dữ liệu vào session
            session.setAttribute("listCart", listCart);
            session.setAttribute("totalMoney", totalMoney);
            session.setAttribute("size", listCart.size());
            session.setAttribute("discount", discount);

            // Lưu dữ liệu vào request để hiển thị ngay trên order.jsp
            request.setAttribute("listCart", listCart);
            request.setAttribute("totalMoney", totalMoney);
            request.setAttribute("size", listCart.size());
            request.setAttribute("discount", discount);

            // Chuyển tiếp đến order.jsp
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
}