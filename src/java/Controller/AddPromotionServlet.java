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
        }

        request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Nhận dữ liệu từ form JSP
            String productIDStr = request.getParameter("productID");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String discountPercentStr = request.getParameter("discountPercent");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            // Kiểm tra dữ liệu đầu vào có bị null hoặc rỗng không
            if (productIDStr == null || name == null || description == null || discountPercentStr == null
                    || startDateStr == null || endDateStr == null
                    || productIDStr.trim().isEmpty() || name.trim().isEmpty()
                    || description.trim().isEmpty() || discountPercentStr.trim().isEmpty()
                    || startDateStr.trim().isEmpty() || endDateStr.trim().isEmpty()) {
                request.getSession().setAttribute("errorModal", "Vui lòng điền đầy đủ thông tin!");
                response.sendRedirect("promotionController");
                return;
            }

            // Chuyển đổi dữ liệu
            int productID = Integer.parseInt(productIDStr);
            double discountPercent = Double.parseDouble(discountPercentStr);

            // Kiểm tra discountPercent có hợp lệ không
            if (discountPercent < 1 || discountPercent > 100) {
                request.getSession().setAttribute("errorModal", "Phần trăm giảm giá không được vượt quá 100");
                response.sendRedirect("promotionController");
                return;
            }

            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            Date createdAt = new Date(System.currentTimeMillis());
            Date updatedAt = new Date(System.currentTimeMillis());

            // Kiểm tra ngày bắt đầu không lớn hơn ngày kết thúc
            if (startDate.after(endDate)) {
                request.getSession().setAttribute("errorModal", "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                response.sendRedirect("promotionController");
                return;
            }

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
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorModal", "Dữ liệu không hợp lệ! Hãy kiểm tra lại số và ngày.");
            response.sendRedirect("promotionController");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorModal", "Có lỗi xảy ra khi thêm khuyến mãi!");
            request.getRequestDispatcher("PromotionManagement.jsp").forward(request, response);
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
