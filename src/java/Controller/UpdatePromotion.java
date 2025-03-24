package Controller;

import DAO.PromotionDAO;
import Model.Promotion;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdatePromotion extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UpdatePromotion.class.getName());
    private PromotionDAO promotionDAO;

    public void init() {
        promotionDAO = new PromotionDAO();
    }

    private Date convertStringToSQLDate(String dateStr) {
        return (dateStr == null || dateStr.isEmpty()) ? null : Date.valueOf(LocalDate.parse(dateStr));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            String promotionIDStr = request.getParameter("promotionID");
            String productIDStr = request.getParameter("productID");
            String name = request.getParameter("name");
            String promotionCode = request.getParameter("promotionCode");
            String description = request.getParameter("description");
            String discountPercentStr = request.getParameter("discountPercent");
            String status = request.getParameter("status");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            // Kiểm tra dữ liệu null hoặc rỗng
            if (promotionIDStr == null || productIDStr == null || name == null
                    || promotionCode == null || description == null || discountPercentStr == null
                    || status == null || startDateStr == null || endDateStr == null
                    || promotionIDStr.trim().isEmpty() || productIDStr.trim().isEmpty()
                    || name.trim().isEmpty() || promotionCode.trim().isEmpty()
                    || description.trim().isEmpty() || discountPercentStr.trim().isEmpty()
                    || status.trim().isEmpty() || startDateStr.trim().isEmpty()
                    || endDateStr.trim().isEmpty()) {
                request.getSession().setAttribute("errorModal", "Vui lòng điền đầy đủ thông tin!");
                response.sendRedirect("promotionController");
                return;
            }

            // Chuyển đổi kiểu dữ liệu
            int promotionID = Integer.parseInt(promotionIDStr);
            int productID = Integer.parseInt(productIDStr);
            double discountPercent = Double.parseDouble(discountPercentStr);

            // Kiểm tra discountPercent có hợp lệ không
            if (discountPercent < 0 || discountPercent > 100) {
                request.getSession().setAttribute("errorModal", "Phần trăm giảm giá phải từ 0 đến 100!");
                response.sendRedirect("promotionController");
                return;
            }

            Date startDate = convertStringToSQLDate(startDateStr);
            Date endDate = convertStringToSQLDate(endDateStr);
            Date updatedAt = new Date(System.currentTimeMillis());

            // Kiểm tra ngày bắt đầu không lớn hơn ngày kết thúc
            if (startDate.after(endDate)) {
                request.getSession().setAttribute("errorModal", "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                response.sendRedirect("promotionController");
                return;
            }

            // Kiểm tra xem promotion đã tồn tại nhưng bỏ qua chính nó khi cập nhật
            if (promotionDAO.isPromotionExistForUpdate(promotionID, name, discountPercent)) {
                request.getSession().setAttribute("errorModal", "Khuyến mãi đã tồn tại.");
                response.sendRedirect("promotionController");
                return;
            }

            // Tạo đối tượng Promotion và cập nhật
            Promotion promotion = new Promotion(promotionID, productID, name, description,
                    discountPercent, startDate, endDate, status,
                    null, updatedAt, promotionCode);
            promotionDAO.updatePromotion(promotion);

            request.getSession().setAttribute("successModal", "Cập nhật khuyến mãi thành công.");
            response.sendRedirect("promotionController");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorModal", "Dữ liệu không hợp lệ! Vui lòng kiểm tra số và ngày.");
            response.sendRedirect("promotionController");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error updating promotion", e);
            request.getSession().setAttribute("errorModal", "Có lỗi khi cập nhật khuyến mãi.");
            response.sendRedirect("promotionController");
        }
    }
}
