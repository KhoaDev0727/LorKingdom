package Controller;

import DAO.PromotionDAO;
import DAO.RoleDAO;
import Model.Product;
import Model.Promotion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class PromotionController extends HttpServlet {

    RoleDAO roleDAO = new RoleDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String indexString = request.getParameter("index");
            if (indexString == null) {
                indexString = "1";
            }

            int index = Integer.parseInt(indexString);
            int promotionPerPage = 9;

            PromotionDAO dao = new PromotionDAO();
            int totalPromotions = dao.getTotalPromotion();
            int endPage = totalPromotions / promotionPerPage;
            if (totalPromotions % promotionPerPage != 0) {
                endPage++;
            }

            List<Promotion> listP = dao.getAllPromotionsPerPage(index, promotionPerPage);
            List<Product> productList = dao.getAllProducts();

            request.setAttribute("productList", productList);
            request.setAttribute("listP", listP);
            request.setAttribute("endPage", endPage);
            request.setAttribute("currentPage", index);

            request.getRequestDispatcher("PromotionController.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userRoleID = (Integer) session.getAttribute("roleID");
        if (userRoleID == null) {
            response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp");
            return;
        } else {
            processRequest(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            PromotionDAO dao = new PromotionDAO();
            // Xóa khuyến mãi
            if ("delete".equals(action)) {
                int proId = Integer.parseInt(request.getParameter("promotionID"));
                dao.deleteOrder(proId);
                response.sendRedirect("promotionController");
                return;
            }

            // Tìm kiếm khuyến mãi theo tên
            if ("search".equals(action)) {
                String proName = request.getParameter("promotionName");
                List<Promotion> list = dao.searchProByName(proName);
                request.setAttribute("listP", list);
                request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
                return;
            }

            // Tìm kiếm khuyến mãi có mức giảm giá lớn hơn giá trị nhập vào
            if ("searchByDiscount".equals(action)) {
                double minDiscount = Double.parseDouble(request.getParameter("minDiscount"));
                List<Promotion> promotionList = dao.searchPromotionByDiscount(minDiscount);
                request.setAttribute("promotionList", promotionList);
                request.setAttribute("minDiscount", minDiscount);
                request.getRequestDispatcher("PromotionSearchResult.jsp").forward(request, response);
                return;
            }

        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại!");
            request.getRequestDispatcher("PromotionController.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
