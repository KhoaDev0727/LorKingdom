package Controller;

import DAO.CartDAO;
import DAO.ProductDAO;
import Model.CartItems;
import Model.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManagementServlet extends HttpServlet {

    private final CartDAO cartDAO;

    public CartManagementServlet() throws SQLException, ClassNotFoundException {
        this.cartDAO = new CartDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null) {
                showCart(request, response);
                return;
            }
            switch (action) {
                case "delete":
                    deleteItem(request, response);
                    break;
                case "deleteAllItem":
                    deleteAllItem(request, response);
                    break;
                default:
                    showCart(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Server error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null) {
                showCart(request, response);
                return;
            }
            switch (action) {
                case "update":
                    updateItem(request, response);
                    break;
                case "add":
                    addItem(request, response);
                    break;
                default:
                    showCart(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Server error");
        }
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            List<CartItems> list = cartDAO.getCartItems(userId);
            if (list == null) {
                list = new ArrayList<>();
            }
            double totalMoney = calculateTotalMoney(list);
            request.setAttribute("listCart", list);
            request.setAttribute("totalMoney", totalMoney);
            request.setAttribute("size", list.size());
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Error loading cart");
        }
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            int productId = Integer.parseInt(request.getParameter("productID"));
            String act = request.getParameter("action");
            String operation = request.getParameter("operation");
            List<CartItems> list = cartDAO.getCartItems(userId);
            CartItems currentItem = null;
            for (CartItems item : list) {
                if (item.getProduct() != null && item.getProduct().getProductID() == productId) {
                    currentItem = item;
                    break;
                }
            }
            if (currentItem == null) {
                sendErrorResponse(response, "Sản phẩm không tìm thấy trong giỏ hàng.");
                return;
            }
            int newQuantity = currentItem.getQuantity();
            if ("increase".equalsIgnoreCase(operation)) {
                newQuantity++;
            } else if ("decrease".equalsIgnoreCase(operation)) {
                newQuantity--;
            }

            boolean success;
            if (newQuantity < 1) {
                success = cartDAO.removeItem(userId, productId);
                newQuantity = 0;
            } else {
                success = cartDAO.updateItem(userId, productId, newQuantity);
            }

            if (!success) {
                sendErrorResponse(response, "Cập nhật giỏ hàng thất bại.");
                return;
            }
            list = cartDAO.getCartItems(userId);
            double totalMoney = calculateTotalMoney(list);
            int cartSize = list.size();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("newQuantity", newQuantity);
            responseData.put("itemTotal", currentItem.getPrice() * newQuantity);
            responseData.put("totalMoney", totalMoney);
            responseData.put("cartSize", cartSize);
            sendJsonResponse(response, responseData);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi cập nhật sản phẩm.");
        }
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            int productID = Integer.parseInt(request.getParameter("productID"));

            boolean rowUpdate = cartDAO.removeItem(userId, productID);
            if (rowUpdate) {
                List<CartItems> list = cartDAO.getCartItems(userId);
                double totalMoney = calculateTotalMoney(list);
                int cartSize = list.size();

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("totalMoney", totalMoney);
                responseData.put("cartSize", cartSize);

                sendJsonResponse(response, responseData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi xóa danh mục.");
        }
    }

    private void deleteAllItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy userId từ session
            int userId = getSession(request, response);
            // Gọi phương thức removeAll trong cartDAO
            boolean rowUpdate = cartDAO.removeAll(userId);
            if (rowUpdate) {
                // Trả về phản hồi JSON nếu xóa thành công
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"totalMoney\": 0, \"cartSize\": 0}");
            } else {
                // Trả về lỗi nếu không xóa được
                sendErrorResponse(response, "Không thể xóa tất cả các mục.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi khi xóa tất cả các mục.");
        }
    }

    private double calculateTotalMoney(List<CartItems> items) {
        double total = 0.0;
        for (CartItems item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(data));
        out.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"error\": \"" + errorMessage + "\"}");
        out.flush();
    }

    public void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            int productId = Integer.parseInt(request.getParameter("productID"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double price = getProductPrice(productId);

            boolean rowUpdate = cartDAO.addItem(userId, productId, quantity);
            if (rowUpdate) {
                showCart(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error adding item\"}");
        }
    }

    private static int getSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
//        return session.getAttribute(2408);
        return 3;
    }

    @Override
    public String getServletInfo() {
        return "Cart Management Servlet";
    }

    private double getProductPrice(int productId) {
        // Lấy giá sản phẩm từ cơ sở dữ liệu
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getProduct(productId);
        return product != null ? product.getPrice() : 0.0;
    }

}
