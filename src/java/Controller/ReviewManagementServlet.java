package Controller;

import DAO.AccountDAO;
import DAO.MyUntilsDAO;
import DAO.ReviewDAO;
import Model.Account;
import Model.Review;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class ReviewManagementServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "reviews";
    private static final String FOLDER = "reviews";
    private static int PAGE = 1;
    private static final int PAGE_SIZE = 10;
    private static final MyUntilsDAO myUntilsDAO = new MyUntilsDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");

            if (action != null) {
                switch (action) {
                    case "delete":
                        deleteReview(request, response);
                        break;
                    case "hardDelete":
                        hardDeleteReview(request, response);
                        break;
                    case "search":
                        searchReview(request, response);
                        break;
                    case "trash":
                        showReviewTrash(request, response);
                        break;
                    case "restore":
                        restoreReview(request, response);
                        break;
                    default:
                        showReview(request, response);
                }
            } else {
                showReview(request, response);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, "Database error: ", e);
            request.setAttribute("errorMessage", "A database error occurred while processing your request.");
            try {
                showReview(request, response); // Không cần try-catch lồng vì SQLException đã xử lý
            } catch (SQLException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, "Unexpected error: ", e);
            request.setAttribute("errorMessage", "An unexpected error occurred while processing your request.");
            try {
                showReview(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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
                    case "delete":
                        deleteReview(request, response);
                        break;
                    case "hardDelete":
                        hardDeleteReview(request, response);
                        break;
                    case "fillterCustomer":
                        getListReviewForCustomer(request, response);
                        break;
                    default:
                        showReview(request, response);
                }
            } else {
                showReview(request, response);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, "Database error: ", e);
            request.setAttribute("errorMessage", "A database error occurred while processing your request.");
            try {
                showReview(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, "Unexpected error: ", e);
            request.setAttribute("errorMessage", "An unexpected error occurred while processing your request.");
            try {
                showReview(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void showReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {
        List<Review> list = new ArrayList<>();
        PAGE = getPageFromRequest(request);
        int totalPages = myUntilsDAO.getTotalPagesReview(PAGE_SIZE, 0); // Chỉ ném SQLException
        list = ReviewDAO.showReview(PAGE, PAGE_SIZE);
        setRequestAttributes(request, list, totalPages, "ReviewManagementServlet");
        request.getRequestDispatcher("ReviewManagement.jsp").forward(request, response);
    }

    private void showReviewTrash(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {
        List<Review> list = new ArrayList<>();
        PAGE = getPageFromRequest(request);
        int totalPages = myUntilsDAO.getTotalPagesReview(PAGE_SIZE, 1); // Chỉ ném SQLException
        list = ReviewDAO.showReviewTrash(PAGE, PAGE_SIZE);
        setRequestAttributes(request, list, totalPages, "ReviewManagementServlet?action=trash");
        request.getRequestDispatcher("ReviewManagement.jsp").forward(request, response);
    }

    private void searchReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {
        try {
            int filterByIdUserProductID = parseIntOrDefault(request.getParameter("filterUserProduct"), 0);
            int filterRating = parseIntOrDefault(request.getParameter("filterRating"), 0);
            int filterStatus = parseIntOrDefault(request.getParameter("filterStatus"), -1);
            PAGE = getPageFromRequest(request);

            int totalPages = myUntilsDAO.getTotalPagesSearchReview(filterRating, filterStatus, filterByIdUserProductID, PAGE_SIZE); // Chỉ ném SQLException
            int offset = (PAGE - 1) * PAGE_SIZE;
            List<Review> listReview = ReviewDAO.searchReview(filterRating, filterStatus, filterByIdUserProductID, offset, PAGE_SIZE);
            request.setAttribute("reviews", listReview);
            request.setAttribute("filterByIdUserProductID", filterByIdUserProductID);
            request.setAttribute("filterRating", filterRating);
            request.setAttribute("filterStatus", filterStatus);
            request.setAttribute("action", "search");
            setRequestAttributes(request, listReview, totalPages, "ReviewManagementServlet");
            request.getRequestDispatcher("ReviewManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, "Database error during search: ", e);
            request.setAttribute("errorMessage", "Error during search due to database issue.");
            showReview(request, response);
        } catch (Exception e) {
            Logger.getLogger(ReviewManagementServlet.class.getName()).log(Level.SEVERE, "Unexpected error during search: ", e);
            request.setAttribute("errorMessage", "Unexpected error during search.");
            showReview(request, response);
        }
    }

    // Các hàm còn lại giữ nguyên vì không liên quan trực tiếp đến MyUntilsDAO
    private void restoreReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            boolean restored = ReviewDAO.UpdateStatusIsDeletedReview(reviewID, 0);
            if (!restored) {
                session.setAttribute("errorMessage", "Đánh giá của bạn đã khôi phục thất bại.");
            } else {
                session.setAttribute("successMessage", "Đánh giá của bạn đã được khôi phục.");
            }
            response.sendRedirect("ReviewManagementServlet?action=trash");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ReviewManagementServlet?action=trash");
        }
    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            boolean deleted = ReviewDAO.UpdateStatusIsDeletedReview(reviewID, 1);
            if (deleted) {
                session.setAttribute("successMessage", "Đánh giá đã được chuyển vào thùng rác thành công.");
            } else {
                session.setAttribute("errorMessage", "Không thể di chuyển đánh giá vào thùng rác.");
            }
            response.sendRedirect("ReviewManagementServlet");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect("ReviewManagementServlet");
        }
    }

    private void hardDeleteReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            boolean deleted = ReviewDAO.deleteReview(reviewID);
            response.sendRedirect("ReviewManagementServlet?action=trash");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Đã xảy ra lỗi.");
            session.setAttribute("messageType", "danger");
            response.sendRedirect("ReviewManagementServlet?action=trash");
        }
    }

    private void updateStatusReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {
        HttpSession session = request.getSession();
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            int status = Integer.parseInt(request.getParameter("status"));
            boolean updated = ReviewDAO.UpdateStatusReview(reviewID, status);

            if (updated) {
                session.setAttribute("successMessage", "Đánh giá của bạn đã được cập nhật thành công.");
            } else {
                session.setAttribute("errorMessage", "Đánh giá của bạn  câp nhật thất bại.");
            }
            showReview(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "An error occurred");
            session.setAttribute("messageType", "danger");
            showReview(request, response);
        }
    }

    private void addReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        HttpSession session = request.getSession();
        response.setContentType("application/json"); // Set response type to JSON
        response.setCharacterEncoding("UTF-8");      // Ensure proper encoding

        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            int productId = Integer.parseInt(request.getParameter("productId"));
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int accountId = (Integer) session.getAttribute("userID");
            int orderDetailID = Integer.parseInt(request.getParameter("orderDetailID"));
            System.out.println(orderDetailID);
             System.out.println(orderId);
              System.out.println(productId);
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment"); // Note: JavaScript sends "comment", not "description"

            Part filePart = request.getPart("image");
            String image = null;
            if (filePart != null && filePart.getSize() > 0) {
                image = UploadImage.uploadFile(filePart, uploadPath, FOLDER);
            }

            Review review = new Review(accountId, productId, image, rating, comment);
            boolean added = ReviewDAO.addReview(review);

            if (added) {
                int reviewed = 1; 
                boolean uploadRviewed = ReviewDAO.UpdateStatusReviewed(orderDetailID, reviewed, productId, orderId );
                if (uploadRviewed) {
                    response.getWriter().write("{\"success\": true}");
                }
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể thêm đánh giá\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra khi bạn đánh giá sản phẩm\"}");
        }   
    }

    // Helper methods
    private int getPageFromRequest(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        return (pageParam != null && !pageParam.trim().isEmpty()) ? Math.max(1, Integer.parseInt(pageParam.trim())) : 1;
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void setRequestAttributes(HttpServletRequest request, List<Review> reviews, int totalPages, String forward) {
        request.setAttribute("reviews", reviews);
        request.setAttribute("currentPage", PAGE);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("forward", forward);
    }

    protected void getListReviewForCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        int star = Integer.parseInt(request.getParameter("star"));
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = 5;
        int totalReviews = ReviewDAO.getTotalReviews(star, productID);
        int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

        List<Review> reviews = ReviewDAO.getReviewsFromDatabase(star, productID, (page - 1) * pageSize, pageSize);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.add("reviews", new Gson().toJsonTree(reviews));
        jsonResponse.addProperty("currentPage", page);
        jsonResponse.addProperty("totalPages", totalPages);
        System.out.println(new Gson().toJsonTree(reviews));
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

}
