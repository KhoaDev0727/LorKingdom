/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AccountDAO;
import Model.Product;
import DAO.ProductDAO;
import DAO.ProductImageDAO;
import DAO.MaterialDAO;
import DAO.BrandDAO;
import DAO.PriceRangeDAO;
import DAO.SexDAO;
import DAO.AgeDAO;
import DAO.SuperCategoryDAO;
import DAO.CategoryDAO;
import DAO.ReviewDAO;
import Model.Account;
import Model.Review;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin1
 */
public class getList extends HttpServlet {

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
            out.println("<title>Servlet getList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getList at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        AgeDAO ageDAO = new AgeDAO();
        SexDAO sexDAO = new SexDAO();
        PriceRangeDAO priceRangeDAO = new PriceRangeDAO();
        BrandDAO brandDAO = new BrandDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        ProductDAO productDAO = new ProductDAO();
        ProductImageDAO productImageDAO = new ProductImageDAO();

        // Số sản phẩm mỗi trang (bạn có thể đổi thành 12 nếu muốn)
        int itemsPerPage = 9;
        // Trang hiện tại (mặc định là 1)
        int page = 1;

        // Lấy tham số page từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        // Tính offset
        int offset = (page - 1) * itemsPerPage;

        try {
            // Lấy các tham số filter
            Integer categoryID = parseIntOrNull(request.getParameter("categoryID"));
            Integer ageID = parseIntOrNull(request.getParameter("ageID"));
            Integer sexID = parseIntOrNull(request.getParameter("sexID"));
            Integer priceRangeID = parseIntOrNull(request.getParameter("priceRangeID"));
            Integer brandID = parseIntOrNull(request.getParameter("brandID"));
            boolean hasFilters = (categoryID != null || ageID != null || sexID != null
                    || priceRangeID != null || brandID != null);
            List<Product> allProducts;
            String search = (String) request.getAttribute("search");
            if (search != null && !search.trim().isEmpty()) {
                allProducts = productDAO.searchProducts(search.trim().toLowerCase());
            } else if (hasFilters) {
                allProducts = productDAO.getFilteredProducts(categoryID, ageID, sexID,
                        priceRangeID, brandID);
            } else {
                allProducts = productDAO.getAllProducts();
            }
            // Tính tổng số sản phẩm
            int totalProducts = allProducts.size();
            // Tính tổng số trang
            int totalPages = (int) Math.ceil((double) totalProducts / itemsPerPage);

            // Đảm bảo offset hợp lệ
            if (offset < 0) {
                offset = 0;
            }
            if (offset > totalProducts) {
                offset = totalProducts;
            }

            // endIndex = offset + itemsPerPage
            int endIndex = offset + itemsPerPage;
            if (endIndex > totalProducts) {
                endIndex = totalProducts;
            }
            // Cắt danh sách cho trang hiện tại
            List<Product> productsPerPage = allProducts.subList(offset, endIndex);
            for (Product product : productsPerPage) {
                String name = product.getName();
                if (name != null && name.length() > 20) {
                    product.setName(name.substring(0, 20) + "...");
                }
            }
            Map<Integer, Double> mediumRatingMap = new HashMap<>();
            for (Product product : productsPerPage) { // Duyệt từng sản phẩm
                List<Review> listReviews = ReviewDAO.showReviewForCustomer(product.getProductID());

                int rating5 = 0, rating4 = 0, rating3 = 0, rating2 = 0, rating1 = 0;
                int totalReview = listReviews.size();

                for (Review review : listReviews) {
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

                double mediumRating = (totalReview > 0)
                        ? (double) (rating5 * 5 + rating4 * 4 + rating3 * 3 + rating2 * 2 + rating1 * 1) / totalReview
                        : 0;

                // Lưu vào map với key là productID
                mediumRatingMap.put(product.getProductID(), mediumRating);
            }

            // Đưa danh sách đánh giá trung bình vào request để chuyển sang JSP
            request.setAttribute("mediumRatingMap", mediumRatingMap);

            // Het
            request.setAttribute("listP", productsPerPage);
            request.setAttribute("totalProducts", totalProducts);

            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("forward", request.getContextPath() + "/getList");

            request.setAttribute("superCategories", superCategoryDAO.getActiveSuperCategories());
            request.setAttribute("categories", categoryDAO.getAllCategories());
            request.setAttribute("ages", ageDAO.getAllAges());
            request.setAttribute("listS", sexDAO.getAllSexes());
            request.setAttribute("listB", brandDAO.getActiveBrand());
            request.setAttribute("listM", materialDAO.getAllActiveMaterials());
            request.setAttribute("listPriceRanges", priceRangeDAO.getAllActivePriceRanges());
            request.setAttribute("mainImages", ProductImageDAO.getMainProductImages());

            String partial = request.getParameter("partial");
            if ("true".equals(partial)) {
                request.getRequestDispatcher("/assets/Component/partialProduct.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("home.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private Integer parseIntOrNull(String val) {
        if (val == null || val.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
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
        String searchKeyword = request.getParameter("search");

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            request.setAttribute("search", searchKeyword.toLowerCase());
        }
        doGet(request, response);
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
