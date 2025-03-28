/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AccountDAO;
import DAO.OriginDAO;
import DAO.MaterialDAO;
import DAO.BrandDAO;
import DAO.SexDAO;
import DAO.AgeDAO;
import DAO.CategoryDAO;
import DAO.ProductDAO;
import DAO.ProductImageDAO;
import DAO.PromotionDAO;
import DAO.ReviewDAO;
import Model.Account;
import Model.Product;
import Model.ProductImage;
import Model.Promotion;
import Model.Review;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author admin1
 */
public class ProductDetailServlet extends HttpServlet {

    ProductDAO productDAO = new ProductDAO();

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        try {
            String productIdStr = request.getParameter("productID");
            String pageStr = request.getParameter("page");

            // Kiểm tra productID hợp lệ
            if (productIdStr == null || productIdStr.isEmpty()) {
                response.sendRedirect("home.jsp");
                return;
            }

            int productId = Integer.parseInt(productIdStr);

            // Xử lý số trang (mặc định là trang 1 nếu không có giá trị)
            int page = 1;
            int pageSize = 5;
            if (pageStr != null) {
                try {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) {
                        page = 1; // Đảm bảo không có giá trị âm
                    }
                } catch (NumberFormatException e) {
                    page = 1; // Nếu có lỗi chuyển đổi số, đặt trang về 1
                }
            }

            // Lấy thông tin sản phẩm
            Product product = ProductDAO.getAvailableProductById(productId);
            if (product == null) {
                // Sản phẩm không khả dụng (ví dụ: chất liệu đã bị xóa mềm), chuyển hướng về trang home
                response.sendRedirect(request.getContextPath() + "/getList");
                return;
            }

            // Khởi tạo các DAO khi cần thiết
            CategoryDAO categoryDAO = new CategoryDAO();
            OriginDAO originDAO = new OriginDAO();
            AgeDAO ageDAO = new AgeDAO();
            BrandDAO brandDAO = new BrandDAO();
            MaterialDAO materialDAO = new MaterialDAO();
            SexDAO sexDAO = new SexDAO();
            ProductImageDAO productImageDAO = new ProductImageDAO();
            ProductDAO productDAO = new ProductDAO();
            PromotionDAO promotion = new PromotionDAO();

            // Set các thuộc tính sản phẩm
            request.setAttribute("product", product);
            request.setAttribute("category", categoryDAO.getCategoryNameByProductId(productId));
            request.setAttribute("origin", originDAO.getOriginNameByProductId(productId));
            request.setAttribute("age", ageDAO.getAgeRangeByProductId(productId));
            request.setAttribute("brand", brandDAO.getBrandNameByProductId(productId));
            request.setAttribute("material", materialDAO.getMaterialNameByProductId(productId)); // Fix typo
            request.setAttribute("sex", sexDAO.getSexNameByProductId(productId));
            String originName = materialDAO.getMaterialNameByProductId(productId);

// In ra console để kiểm tra
            System.out.println("Material name: " + originName);
            // Lấy ảnh sản phẩm
            request.setAttribute("mainImages", productImageDAO.getMainProductImages());
            request.setAttribute("listImages", productImageDAO.getSecondaryProductImagesByProductId(productId));
// ======================== Hien thi review =========
            int rating5 = 0, rating4 = 0, rating3 = 0, rating2 = 0, rating1 = 0;
            int totalComment = 0, totalImage = 0;
            List<Review> listReviews = ReviewDAO.showReviewForCustomer(productId); // Sử dụng productId thực tế
            List<Account> inforCustomers = new ArrayList<>();

            for (Review review : listReviews) {
                Account account = AccountDAO.getInforAccountByID(review.getAccountID());
                if (account != null && !inforCustomers.contains(account)) {
                    inforCustomers.add(account);
                }
                if (!review.getComment().isEmpty()) {
                    totalComment++;
                }
                if (review.getImgReview() != null && !review.getImgReview().isEmpty()) {
                    totalImage++;
                }

                switch (review.getRating()) {
                    case 5:
                        rating5++;
                        break;
                    case 4:
                        rating4++;
                        break;
                    case 3:
                        rating3++;
                        break;
                    case 2:
                        rating2++;
                        break;
                    default:
                        rating1++;
                        break;
                }
            }

            int totalReview = listReviews.size();
            double mediumRating = totalReview > 0
                    ? (double) (rating5 * 5 + rating4 * 4 + rating3 * 3 + rating2 * 2 + rating1 * 1) / totalReview
                    : 0;
            request.setAttribute("mediumRatings", mediumRating);
            request.setAttribute("rating5", rating5);
            request.setAttribute("rating4", rating4);
            request.setAttribute("rating3", rating3);
            request.setAttribute("rating2", rating2);
            request.setAttribute("rating1", rating1);
            request.setAttribute("listReviews", listReviews);
            request.setAttribute("totalComment", totalComment);
            request.setAttribute("totalImage", totalImage);
            // LOC CODE KHONG LOI DAU
            List<Promotion> promotions = promotion.getPromotionsByProductId(productId);
            double discountPrice = product.getPrice();  // Giá gốc
            boolean hasPromotion = false;
            if (promotions != null && !promotions.isEmpty()) {
                // Nếu có khuyến mãi, tính giá sau giảm
                discountPrice = calculateDiscountedPrice(product.getPrice(), promotions);
                hasPromotion = true;
            }
            request.setAttribute("hasPromotion", hasPromotion);  // Cập nhật thông tin có khuyến mãi hay không
            request.setAttribute("originalPrice", product.getPrice());
            request.setAttribute("discountPrice", discountPrice);
            // LOC CODE KHONG LOI DAU
            // Lấy sản phẩm liên quan
            int categoryID = product.getCategoryID();
            List<Product> relatedProducts = productDAO.getRelatedProductsByCategory(categoryID, productId, 8);
            request.setAttribute("relatedProducts", relatedProducts);

            // Điều hướng đến trang chi tiết sản phẩm
            request.getRequestDispatcher("ProductDetailPage.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi chuyển đổi số: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "getList");
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi không tìm thấy class: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "getList");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi! Vui lòng thử lại sau.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
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
        processRequest(request, response);
    }

    @WebServlet("/ValidateQuantityServlet")
    public static class ValidateQuantityServlet extends HttpServlet {

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            // Đọc JSON từ request
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonRequest = new JSONObject(sb.toString());

            // Lấy số lượng & productID từ client
            int quantity = jsonRequest.getInt("quantity");
            int productID = jsonRequest.getInt("productID");

            // Lấy số tồn kho thực từ DB
            int stock = 0;
            try {
                stock = ProductDAO.getStockQuantity(productID);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ValidateQuantityServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonResponse = new JSONObject();

            if (quantity > stock) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Số lượng vượt quá số sản phẩm hiện có: " + stock);
                jsonResponse.put("validQuantity", stock);
            }
            response.getWriter().write(jsonResponse.toString());
        }
    }

    private double calculateDiscountedPrice(double originalPrice, List<Promotion> promotions) {
        double discountPrice = originalPrice;  // Giá gốc

        if (promotions != null && !promotions.isEmpty()) {
            // Giả sử lấy tỷ lệ giảm của khuyến mãi đầu tiên
            double discountRate = promotions.get(0).getDiscountPercent();  // Lấy tỷ lệ giảm (giả sử từ Promotion)
            if (discountRate > 0) {
                discountPrice = originalPrice * (1 - discountRate / 100);  // Tính giá sau giảm
                return discountPrice;
            }
        }

        return originalPrice;
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



