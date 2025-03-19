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
import DAO.ReviewDAO;
import Model.Account;
import Model.Product;
import Model.ProductImage;
import Model.Review;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Product product = ProductDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect("home.jsp");
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

            // Set các thuộc tính sản phẩm
            request.setAttribute("product", product);
            request.setAttribute("category", categoryDAO.getCategoryNameByProductId(productId));
            request.setAttribute("origin", originDAO.getOriginNameByProductId(productId));
            request.setAttribute("age", ageDAO.getAgeRangeByProductId(productId));
            request.setAttribute("brand", brandDAO.getBrandNameByProductId(productId));
            request.setAttribute("material", materialDAO.getMaterialNameByProductId(productId)); // Fix typo
            request.setAttribute("sex", sexDAO.getSexNameByProductId(productId));

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

            // Lấy sản phẩm liên quan
            int categoryID = product.getCategoryID();
            List<Product> relatedProducts = productDAO.getRelatedProductsByCategory(categoryID, productId, 8);
            request.setAttribute("relatedProducts", relatedProducts);

            // Điều hướng đến trang chi tiết sản phẩm
            request.getRequestDispatcher("ProductDetailPage.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Log lỗi và chuyển hướng đến danh sách sản phẩm nếu có lỗi định dạng số
            System.err.println("Lỗi chuyển đổi số: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/getList");

        } catch (ClassNotFoundException e) {
            // Log lỗi nếu thiếu class
            System.err.println("Lỗi không tìm thấy class: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/getList");

        } catch (Exception e) {
            // Log lỗi chi tiết
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
