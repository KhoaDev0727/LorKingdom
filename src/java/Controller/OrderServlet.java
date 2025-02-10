/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import Model.Order;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class OrderServlet extends HttpServlet {

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
        try {

            // Lấy chỉ số trang hiện tại từ request
            String indexString = request.getParameter("index");
            if (indexString == null) {
                indexString = "1"; // Nếu không có trang hiện tại, mặc định là trang 1
            }

            int index = Integer.parseInt(indexString);

            // Số lượng sản phẩm trên mỗi trang
            int ordersPerPage = 5;

            OrderDAO dao = new OrderDAO();

            // Lấy tổng số lượng sản phẩm
            int totalOrders = dao.getTotalOrders();

            // Tính toán tổng số trang
            int endPage = totalOrders / ordersPerPage;
            if (totalOrders % ordersPerPage != 0) {
                endPage++;
            }

            List<Order> listO = dao.getOrdersByPage(index, ordersPerPage);

            request.setAttribute("listO", listO);

            request.setAttribute("endPage", endPage);
            request.setAttribute("currentPage", index);

            request.getRequestDispatcher("OrderHome.jsp").forward(request, response);

        } catch (Exception e) {
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
        processRequest(request, response);
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
        String action = request.getParameter("action");

        if (action != null && action.equals("delete")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            try {
                OrderDAO dao = new OrderDAO();
                dao.deleteOrder(orderId);
                response.sendRedirect("orderServlet");
            } catch (Exception e) {
            }
        }
        if (action != null && action.equals("search")) {
            String customerName = request.getParameter("customerName"); // Lấy tên khách hàng từ input
            try {
                OrderDAO dao = new OrderDAO();
                List<Order> listO = dao.searchByCustomerName(customerName); // Gọi phương thức tìm kiếm
                request.setAttribute("listO", listO);
                request.getRequestDispatcher("OrderHome.jsp").forward(request, response); // Hiển thị kết quả
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
