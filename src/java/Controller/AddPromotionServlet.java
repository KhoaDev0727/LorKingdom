/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.PromotionDAO;
import Model.Product;
import Model.Promotion;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Acer
 */
public class AddPromotionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
             
            PromotionDAO dao = new PromotionDAO();       
            List<Product> productList = dao.getAllProducts(); // Lấy danh sách sản phẩm
            request.setAttribute("productList", productList);
        } catch (Exception e) {
        }

        request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Nhận dữ liệu từ form JSP
            int productID = Integer.parseInt(request.getParameter("productID"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            String status = request.getParameter("status");
            Date createdAt = new Date(System.currentTimeMillis());
            Date updatedAt = new Date(System.currentTimeMillis());

            // Tạo đối tượng Promotion
            Promotion pro = new Promotion();
            pro.setProductID(productID);
            pro.setName(name);
            pro.setDescription(description);
            pro.setDiscountPercent(discountPercent);
            pro.setStartDate(startDate);
            pro.setEndDate(endDate);
            pro.setStatus(status);
            pro.setCreatedAt(createdAt);
            pro.setUpdatedAt(updatedAt);

            // Gọi DAO để thêm vào database
            PromotionDAO promotionDAO = new PromotionDAO();

            if (promotionDAO.isPromotionExist(name, discountPercent)) {
                // Nếu promotion với cùng tên và cùng % giảm giá đã tồn tại, không thêm vào
                request.setAttribute("errorModal", "Khuyến mãi đã tồn tại với cùng tên và phần trăm giảm giá!");
                request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
                return;
            } else if (promotionDAO.isPromotionWithNameExist(name)) {
                // Nếu đã có promotion với tên này nhưng khác % giảm giá, vẫn cho phép thêm
                promotionDAO.addPromotion(pro);
                response.sendRedirect("promotionController");
                return;
            } else {
                // Nếu chưa có promotion nào trùng tên, thêm bình thường
                promotionDAO.addPromotion(pro);
                response.sendRedirect("promotionController");
            }

        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorModal", "Khuyến mãi đã tồn tại với cùng tên và phần trăm giảm giá!");
            request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
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
