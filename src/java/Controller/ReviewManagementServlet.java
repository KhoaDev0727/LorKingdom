/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AccountDAO;
import DAO.MyUntilsDAO;
import DAO.ReviewDAO;
import Model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import Model.Review;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Truong Van Khang - CE181852
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class ReviewManagementServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "reviews";
    private static final String FOLDER = "imageReviews";
    private static int PAGE = 1; // Mặc định là trang 1
    private static int PAGE_SIZE = 10; // Số lượng bản ghi trên mỗi trang
    private static final MyUntilsDAO myUntilsDAO = new MyUntilsDAO();

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
            out.println("<title>Servlet ReviewManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReviewManagementServlet at " + request.getContextPath() + "</h1>");
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
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "viewReview":
                        viewReviewCustomer(request, response);
                        break;
                    case "delete":
                        deleteReview(request, response);
                        break;
                    case "search":
                        searchReview(request, response);
                        break;
                    default:
                        showReview(request, response);
                }
            } else {
                showReview(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "update":
                        updateStatusReview(request, response);
                        break;
                    case "add":
                        addReview(request, response);
                        break;
                    default:
                        showReview(request, response);
                }
            } else {
                showReview(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void viewReviewCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int rating5 = 0, rating4 = 0, rating3 = 0, rating2 = 0, rating1 = 0;
            int totalRating = 0, totalComment = 0, totalImage = 0;
            int productID = 8;
            List<Review> listReviews = ReviewDAO.showReviewForCustomer(productID);
            int totalReview = listReviews.size();
            List<Account> inforCustomers = new ArrayList<>();
            for (Review review : listReviews) {
                Account account = AccountDAO.getInforAccountByID(review.getAccountID());
                if (!inforCustomers.contains(account)) { // Tránh trùng lặp
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
            totalRating = rating5 * 5 + rating4 * 4 + rating3 * 3 + rating2 * 2 + rating1 * 1;
            double mediumRating = (totalReview > 0) ? ((double) totalRating / totalReview) : 0;
            // Tránh lỗi chia 0
            request.setAttribute("mediumRatings", mediumRating);
            request.setAttribute("rating5", rating5);
            request.setAttribute("rating4", rating4);
            request.setAttribute("rating3", rating3);
            request.setAttribute("rating2", rating2);
            request.setAttribute("rating1", rating1);
            request.setAttribute("listReviews", listReviews);
            request.setAttribute("totalComment", totalComment);
            request.setAttribute("totalImage", totalImage);
            request.setAttribute("inforCustomers", inforCustomers);
            request.getRequestDispatcher("ViewReview.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Review> list = new ArrayList<>();
            // Lấy giá trị page từ request nếu có
            if (request.getParameter("page") != null) {
                PAGE = Integer.parseInt(request.getParameter("page"));
            }
            int totalPages = myUntilsDAO.getTotalPagesReview(PAGE_SIZE);
            list = ReviewDAO.showReview(PAGE, PAGE_SIZE);
            request.setAttribute("reviews", list);
            request.setAttribute("currentPage", PAGE);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("forward", "ReviewManagementServlet");
            request.getRequestDispatcher("ReviewManagement.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            boolean deleted = ReviewDAO.deleteReview(reviewID);
            if (deleted) {
                showReview(request, response);
            } else {
                System.out.println("sout");
            }
        } catch (Exception e) {
        }
    }

    private void searchReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            int filterByIdUserProductID = 0;
            PAGE = 1;
            try {
                if (request.getParameter("filterUserProduct") != null) {
                    filterByIdUserProductID = Integer.parseInt(request.getParameter("filterUserProduct"));
                }
            } catch (Exception e) {
                // Bỏ qua lỗi chuyển đổi
            }
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    PAGE = Integer.parseInt(pageParam.trim());
                    if (PAGE < 1) {
                        PAGE = 1;
                    }
                } catch (NumberFormatException e) {
                    PAGE = 1;
                }
            }
            // Giả sử PAGE_SIZE là hằng số đã được định nghĩa

            int filterRating = Integer.parseInt(request.getParameter("filterRating"));
            int filterStatus = Integer.parseInt(request.getParameter("filterStatus"));
            int totalPages = myUntilsDAO.getTotalPagesSearchReview(filterRating, filterStatus, filterByIdUserProductID, PAGE_SIZE);
            // Tính OFFSET từ số trang hiện tại
            int offset = (PAGE - 1) * PAGE_SIZE;
            // Gọi DAO với offset và limit (PAGE_SIZE)
            List<Review> listReview = ReviewDAO.searchReview(filterRating, filterStatus, filterByIdUserProductID, offset, PAGE_SIZE);
            request.setAttribute("reviews", listReview);
            request.setAttribute("filterByIdUserProductID", filterByIdUserProductID);
            request.setAttribute("filterRating", filterRating);
            request.setAttribute("filterStatus", filterStatus);
            request.setAttribute("action", "search");
            request.setAttribute("currentPage", PAGE);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("forward", "ReviewManagementServlet");
            request.getRequestDispatcher("ReviewManagement.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatusReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            int Status = Integer.parseInt(request.getParameter("status"));
            boolean updateRow = ReviewDAO.UpdateStatusReview(reviewID, Status);
            if (updateRow) {
                showReview(request, response);
            } else {
                System.out.println("loi r");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            int productId = 8;  // Consider getting this from request parameters if needed
            int accountId = 2839;   // Consider getting this dynamically from session or request
            // Fix typo in variable name "ranting" -> "rating"
            int rating = Integer.parseInt(request.getParameter("rating"));
            // Fix variable naming: "Comment" -> "comment" (Java convention: variables start with lowercase)
            String comment = request.getParameter("description");
            // Handle file upload properly
            Part filePart = request.getPart("image");
            String image = null; // Initialize image to null
            if (filePart != null && filePart.getSize() > 0) {
                image = UploadImage.uploadFile(filePart, uploadPath, FOLDER);
            }
            // Create Review object
            Review review = new Review(accountId, productId, image, rating, comment);
            // Call DAO method to insert review
            boolean updateRow = ReviewDAO.addReview(review);
            if (updateRow) {
                showReview(request, response);
            } else {
                System.out.println("Error: Failed to add review.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the error to see what went wrong
        }
    }

}
