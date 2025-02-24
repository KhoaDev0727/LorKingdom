/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import Model.Order;
import Model.OrderDetail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Acer
 */
public class OrderView extends HttpServlet {

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

        String action = request.getParameter("action");
        if (action != null && action.equals("view")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            try {
                OrderDetailDAO dao = new OrderDetailDAO();
                List<OrderDetail> list = dao.getOrderDetailById(orderId);
                request.setAttribute("listDetail", list);
                request.getRequestDispatcher("OrderDetails.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        // Kiểm tra xem có phải là tìm kiếm theo số tiền hay không
        if (action != null && action.equals("money")) {
            String minAmountStr = request.getParameter("minAmount");

            // Kiểm tra xem giá trị minAmountStr có hợp lệ hay không
            if (minAmountStr != null && !minAmountStr.isEmpty()) {
                try {
                    // Chuyển đổi minAmountStr thành BigDecimal
                    Double minAmount = new Double(minAmountStr);

                    // Gọi phương thức tìm kiếm từ DAO
                    OrderDAO dao = new OrderDAO();
                    List<Order> list = dao.searchByMoney(minAmount);

                    // Lưu kết quả vào request và chuyển tiếp tới trang JSP
                    request.setAttribute("listO", list); // Lưu kết quả tìm kiếm vào request
                    request.getRequestDispatcher("OrderManagement.jsp").forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    // Xử lý lỗi nếu có
                    request.setAttribute("message", "Error: " + e.getMessage());
                    request.setAttribute("messageType", "danger");
                    request.getRequestDispatcher("OrderManagement.jsp").forward(request, response);
                }
            } else {
                // Nếu minAmountStr rỗng, thông báo lỗi
                request.setAttribute("message", "Minimum Amount cannot be empty");
                request.setAttribute("messageType", "danger");
                request.getRequestDispatcher("OrderManagement.jsp").forward(request, response);
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
