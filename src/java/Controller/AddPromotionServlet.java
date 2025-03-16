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
import java.security.SecureRandom;
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
            e.printStackTrace();
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
            Date createdAt = new Date(System.currentTimeMillis());
            Date updatedAt = new Date(System.currentTimeMillis());

            // **Tạo mã khuyến mãi ngẫu nhiên**
            String promotionCode = generatePromotionCode();

            // Đặt trạng thái mặc định là "Active"
            String status = "Active";

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
            pro.setPromotionCode(promotionCode); // **Thêm mã khuyến mãi vào đối tượng**

            // Gọi DAO để thêm vào database
            PromotionDAO promotionDAO = new PromotionDAO();

            if (promotionDAO.isPromotionExist(name, discountPercent)) {
                request.getSession().setAttribute("errorModal", "Khuyến mãi đã tồn tại với cùng tên và phần trăm giảm giá!");
                response.sendRedirect("promotionController");
                return;
            } else {
                promotionDAO.addPromotion(pro);
                request.getSession().setAttribute("successModal", "Thêm khuyến mãi thành công!");
                response.sendRedirect("promotionController");
            }
        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorModal", "Có lỗi xảy ra khi thêm khuyến mãi!");
            request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
        }
    }

    private String generatePromotionCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        // 6 chữ cái viết hoa
        for (int i = 0; i < 6; i++) {
            char letter = (char) ('A' + random.nextInt(26));
            code.append(letter);
        }

        // 2 số
        for (int i = 0; i < 2; i++) {
            char digit = (char) ('0' + random.nextInt(10));
            code.append(digit);
        }

        return code.toString();
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
