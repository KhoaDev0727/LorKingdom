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
            int promotionID = Integer.parseInt(request.getParameter("promotionID"));
            int productID = Integer.parseInt(request.getParameter("productID"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));
            String status = request.getParameter("status");

            Date startDate = convertStringToSQLDate(request.getParameter("startDate"));
            Date endDate = convertStringToSQLDate(request.getParameter("endDate"));
            Date updatedAt = new Date(System.currentTimeMillis());

            // Kiểm tra xem promotion đã tồn tại chưa
            if (promotionDAO.isPromotionExist(name, discountPercent)) {
                request.getSession().setAttribute("errorModal", "Promotion already exists with the same name and discount percent.");
                response.sendRedirect("promotionController");
                return;
            }

            Promotion promotion = new Promotion(promotionID, productID, name, description, discountPercent, startDate, endDate, status, null, updatedAt);
            promotionDAO.updatePromotion(promotion);

            request.getSession().setAttribute("successModal", "Update Successfully");
            response.sendRedirect("promotionController");
        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error updating promotion", e);
            request.getSession().setAttribute("errorModal", "An error occurred while updating the promotion.");
            response.sendRedirect("promotionController");
        }
    }
}
