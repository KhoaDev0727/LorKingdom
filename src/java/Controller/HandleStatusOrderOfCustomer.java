/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
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
            System.out.println("khanglo");
            System.out.println(new Gson().toJson(orderData));
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
        response.setContentType("application/json"); // Đặt kiểu phản hồi là JSON
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy orderID từ request
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            System.out.println("Received orderID: " + orderID);
            // Gọi service hoặc DAO để hủy đơn hàng
            boolean isCancelled = cancelOrder(orderID);

            // Trả về kết quả dưới dạng JSON
            PrintWriter out = response.getWriter();
            out.print("{\"success\": " + isCancelled + "}");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về thông báo lỗi nếu có ngoại lệ
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false}");
            out.flush();
        }
    }

    public boolean cancelOrder(int orderID) throws SQLException, ClassNotFoundException {
        // Giả sử bạn có một phương thức trong OrderDAO để cập nhật trạng thái đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.updateOrderStatus(orderID, 3);
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
