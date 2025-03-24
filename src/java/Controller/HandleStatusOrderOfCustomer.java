/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class HandleStatusOrderOfCustomer extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HandleStatusOrderOfCustomer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HandleStatusOrderOfCustomer at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        // Lấy chi tiết đơn hàng từ cơ sở dữ liệu
        OrderDAO order = new OrderDAO();
        try {
            java.util.HashMap<String, Object> orderData = order.getOrderDetails(orderId);

            // Chuyển đổi đối tượng order thành JSON và gửi về client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(orderData));
        } catch (SQLException ex) {
            Logger.getLogger(HandleStatusOrderOfCustomer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HandleStatusOrderOfCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    PrintWriter out = response.getWriter(); // Khởi tạo PrintWriter sớm
    boolean isStockReverted = true; // Biến kiểm tra cập nhật stock
    OrderDAO order = new OrderDAO();
    try {
        // Lấy orderID từ request
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        System.out.println("Received orderID: " + orderID);

    
        HashMap<Integer, Integer> rawRestoreProduct = order.getAllProductInOrder(orderID);

 
        // Hủy đơn hàng
        boolean isCancelled = cancelOrder(orderID);
        if (isCancelled) {
            ProductDAO product = new ProductDAO();
            boolean allSuccess = true; // Kiểm tra tất cả sản phẩm có cập nhật thành công không

            for (Map.Entry<Integer, Integer> entry : rawRestoreProduct.entrySet()) {
                int productID = entry.getKey();
                int quantity = entry.getValue();
                isStockReverted = product.revertStockOnCancel(productID, quantity);
                if (!isStockReverted) {
                    System.out.println("Không thể cập nhật số lượng cho sản phẩm ID: " + productID);
                    allSuccess = false;
                    break; // Thoát vòng lặp nếu gặp lỗi
                }
            }
            // Nếu có lỗi, rollback các sản phẩm đã cập nhật trước đó
            if (!allSuccess) {
                System.out.println("Có lỗi xảy ra! Cần thực hiện rollback.");
                isStockReverted = false; // Đánh dấu thất bại
            }
        } else {
            isStockReverted = false; // Hủy đơn hàng thất bại
        }
        // Trả về kết quả dưới dạng JSON
        if (isStockReverted) {
            out.print("{\"success\": true, \"message\": \"Đơn hàng đã được hủy thành công.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Không thể cập nhật số lượng sản phẩm hoặc hủy đơn hàng.\"}");
        }
    } catch (NumberFormatException e) {
        e.printStackTrace();
        out.print("{\"success\": false, \"message\": \"orderID hoặc quantity không hợp lệ.\"}");
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
        out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
    } catch (Exception e) {
        e.printStackTrace();
        out.print("{\"success\": false, \"message\": \"Có lỗi xảy ra khi xử lý yêu cầu.\"}");
    } finally {
        out.flush();
        out.close(); // Đóng PrintWriter
    }
}


    public boolean cancelOrder(int orderID) throws SQLException, ClassNotFoundException {
        // Giả sử bạn có một phương thức trong OrderDAO để cập nhật trạng thái đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.updateOrderStatus(orderID, 4);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

 

}
