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

            // Validate date format first
            if (!isValidDate(startDateStr) || !isValidDate(endDateStr)) {
                request.getSession().setAttribute("errorModal", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd");
                response.sendRedirect("promotionController");
                return;
            }

            // Chuyển đổi dữ liệu
            int productID = Integer.parseInt(productIDStr);
            double discountPercent = Double.parseDouble(discountPercentStr);

            // Kiểm tra discountPercent có hợp lệ không
            if (discountPercent < 1 || discountPercent > 100) {
                request.getSession().setAttribute("errorModal", "Phần trăm giảm giá phải từ 1 đến 100");
                response.sendRedirect("promotionController");
                return;
            }

            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            Date currentDate = new Date(System.currentTimeMillis());
            Date createdAt = currentDate;
            Date updatedAt = currentDate;

            // Kiểm tra ngày bắt đầu không lớn hơn ngày kết thúc
            if (startDate.after(endDate)) {
                request.getSession().setAttribute("errorModal", "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                response.sendRedirect("promotionController");
                return;
            }

            // Kiểm tra ngày bắt đầu không nhỏ hơn ngày hiện tại
            if (startDate.before(currentDate)) {
                request.getSession().setAttribute("errorModal", "Ngày bắt đầu không thể nhỏ hơn ngày hiện tại!");
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
            pro.setPromotionCode(promotionCode);

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
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorModal", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd");
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

    private boolean isValidDate(String dateStr) {
        try {
            // Simple date format validation (yyyy-MM-dd)
            if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return false;
            }

            // Try to parse the date
            Date.valueOf(dateStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
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
