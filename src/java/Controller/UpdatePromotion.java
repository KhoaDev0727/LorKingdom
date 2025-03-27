package Controller;

import DAO.PromotionDAO;
import Model.Promotion;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    private Date convertStringToSQLDate(String dateStr) throws IllegalArgumentException {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        // First validate the format
        if (!isValidDateFormat(dateStr)) {
            throw new IllegalArgumentException("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd");
        }
        
        // Then parse the date
        try {
            return Date.valueOf(LocalDate.parse(dateStr));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày không hợp lệ. Vui lòng kiểm tra lại");
        }
    }

    private boolean isValidDateFormat(String dateStr) {
        // Validate format yyyy-MM-dd using regex
        return dateStr.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
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

            // Validate promotion name length
            if (name.length() > 100) {
                request.getSession().setAttribute("errorModal", "Tên khuyến mãi không được vượt quá 100 ký tự");
                response.sendRedirect("promotionController");
                return;
            }

            // Validate promotion code format (assuming format: 6 letters + 2 numbers)
            if (!promotionCode.matches("^[A-Z]{6}\\d{2}$")) {
                request.getSession().setAttribute("errorModal", "Mã khuyến mãi phải có 6 chữ cái in hoa và 2 số");
                response.sendRedirect("promotionController");
                return;
            }

            // Validate description length
            if (description.length() > 500) {
                request.getSession().setAttribute("errorModal", "Mô tả không được vượt quá 500 ký tự");
                response.sendRedirect("promotionController");
                return;
            }

            // Chuyển đổi kiểu dữ liệu với validation
            int promotionID;
            int productID;
            double discountPercent;
            
            try {
                promotionID = Integer.parseInt(promotionIDStr);
                productID = Integer.parseInt(productIDStr);
                discountPercent = Double.parseDouble(discountPercentStr);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorModal", "ID sản phẩm và phần trăm giảm giá phải là số");
                response.sendRedirect("promotionController");
                return;
            }

            // Kiểm tra discountPercent có hợp lệ không
            if (discountPercent < 1 || discountPercent > 100) {
                request.getSession().setAttribute("errorModal", "Phần trăm giảm giá phải từ 1 đến 100");
                response.sendRedirect("promotionController");
                return;
            }

            // Validate status
            if (!status.equals("Active") && !status.equals("Inactive") && !status.equals("Expired")) {
                request.getSession().setAttribute("errorModal", "Trạng thái không hợp lệ");
                response.sendRedirect("promotionController");
                return;
            }

            Date startDate;
            Date endDate;
            try {
                startDate = convertStringToSQLDate(startDateStr);
                endDate = convertStringToSQLDate(endDateStr);
            } catch (IllegalArgumentException e) {
                request.getSession().setAttribute("errorModal", e.getMessage());
                response.sendRedirect("promotionController");
                return;
            }

            Date currentDate = new Date(System.currentTimeMillis());

            // Kiểm tra ngày bắt đầu không lớn hơn ngày kết thúc
            if (startDate.after(endDate)) {
                request.getSession().setAttribute("errorModal", "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                response.sendRedirect("promotionController");
                return;
            }

            // Kiểm tra ngày kết thúc không nhỏ hơn ngày hiện tại (nếu status là Active)
            if (status.equals("Active") && endDate.before(currentDate)) {
                request.getSession().setAttribute("errorModal", "Không thể đặt trạng thái Active cho khuyến mãi đã hết hạn");
                response.sendRedirect("promotionController");
                return;
            }

            // Kiểm tra xem promotion đã tồn tại nhưng bỏ qua chính nó khi cập nhật
            if (promotionDAO.isPromotionExistForUpdate(promotionID, name, discountPercent)) {
                request.getSession().setAttribute("errorModal", "Khuyến mãi đã tồn tại với cùng tên và phần trăm giảm giá!");
                response.sendRedirect("promotionController");
                return;
            }

            // Tạo đối tượng Promotion và cập nhật
            Promotion promotion = new Promotion(promotionID, productID, name, description,
                    discountPercent, startDate, endDate, status,
                    null, currentDate, promotionCode);
            promotionDAO.updatePromotion(promotion);

            request.getSession().setAttribute("successModal", "Cập nhật khuyến mãi thành công.");
            response.sendRedirect("promotionController");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorModal", "Dữ liệu không hợp lệ! Vui lòng kiểm tra số và ngày.");
            response.sendRedirect("promotionController");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error updating promotion", e);
            request.getSession().setAttribute("errorModal", "Có lỗi khi cập nhật khuyến mãi: " + e.getMessage());
            response.sendRedirect("promotionController");
        }
    }
}