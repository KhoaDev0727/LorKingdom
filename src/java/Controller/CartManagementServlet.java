package Controller;

import DAO.CartDAO;
import DAO.ProductDAO;
import Model.CartItems;
import Model.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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

@WebServlet("/CartManagementServlet")
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
            sendErrorResponse(response, "Lỗi máy chủ.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null) {
                // Mặc định xử lý thêm sản phẩm khi không có action
                addItem(request, response);
                return;
            }
            switch (action) {
                case "update":
                    updateItem(request, response);
                    break;
                default:
                    addItem(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi máy chủ.");
        }
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            if (userId == -1) {
                response.sendRedirect("login.jsp");
                return;
            }
            List<CartItems> list = cartDAO.getCartItems(userId);
            if (list == null) {
                list = new ArrayList<>();
            }
            for (CartItems cartItems : list) {
                System.out.println(cartItems.getProduct().getMainImageUrl());
            }
            double totalMoney = calculateTotalMoney(list);
            request.setAttribute("listCart", list);
            request.setAttribute("totalMoney", totalMoney);
            request.setAttribute("size", list.size());
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi tải giỏ hàng.");
        }
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
             int userId = getSession(request, response);
            if (userId == -1) {
                response.sendRedirect("login.jsp");
                return;
            }
            int productId = Integer.parseInt(request.getParameter("productID"));
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
                sendErrorResponse(response, "Item not found");
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
                sendErrorResponse(response, "Thêm sản phẩm vào giỏ hàng thất bại.");
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
            sendErrorResponse(response, "Lỗi khi cập nhật số lượng sản phẩm.");
        }
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            if (userId == -1) {
                response.sendRedirect("login.jsp");
                return;
            }
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
            sendErrorResponse(response, "Lỗi khi xóa sản phẩm.");
        }
    }

    private void deleteAllItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            if (userId == -1) {
                response.sendRedirect("login.jsp");
                return;
            }
            boolean rowUpdate = cartDAO.removeAll(userId);

            if (rowUpdate) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"size\": 0,\"totalMoney\": 0, \"cartSize\": 0}");
            } else {
                sendErrorResponse(response, "Lỗi khi xóa tất cả các sản phẩm trong giỏ hàng.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi khi xóa tất cả các sản phẩm trong giỏ hàng.");
        }
    }

    public void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = getSession(request, response);
            if (userId == -1) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            int productId = Integer.parseInt(request.getParameter("productID"));
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            List<CartItems> existingItems = cartDAO.getCartItems(userId);
            boolean itemExists = false;
            
            for (CartItems item : existingItems) {
                if (item.getProduct().getProductID() == productId) {
                    quantity += item.getQuantity();
                    cartDAO.updateItem(userId, productId, quantity);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                boolean rowUpdate = cartDAO.addItem(userId, productId, quantity);
                if (!rowUpdate) {
                    sendErrorResponse(response, "Không thể thêm sản phẩm vào giỏ hàng");
                    return;
                }
            }
            response.sendRedirect("CartManagementServlet");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi khi thêm sản phẩm vào giỏ hàng");
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

    private static int getSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Integer userId = (Integer) session.getAttribute("userID");
        return (userId != null) ? userId : -1;
    }

    private double getProductPrice(int productId) {
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getProduct(productId);
        return product != null ? product.getPrice() : 0.0;
    }

    @Override
    public String getServletInfo() {
        return "Cart Management Servlet";
    }
}