/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Model.Review;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class ReviewDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    private static String SELECT_TRASH = "SELECT * FROM Reviews WHERE IsDeleted = 1 ORDER BY ReviewID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
    private static String SELECT_REVIEW = "SELECT * FROM Reviews WHERE IsDeleted = 0 ORDER BY ReviewID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
    private static String SELECT_REVIEW_FOR_CUSTOMER = "SELECT AccountID, ProductID, ImgReview,Rating, Comment,ReviewedAt FROM Reviews WHERE ProductID = ? AND Status = 0 AND IsDeleted = 0 ";
    private static String UPDATE_STATUS = "UPDATE Reviews SET Status = ? WHERE ReviewID = ?;";
    private static String UPDATE_ISDELETED = "UPDATE Reviews SET IsDeleted = ? WHERE ReviewID = ?;";
    private static String DELETE_REVIEW_BY_ID = "DELETE FROM Reviews WHERE ReviewID = ?;";
    private static String SELECT_FILLTER = "SELECT * FROM Reviews WHERE ";
    private static String ADD_PREVIEW = "INSERT INTO Reviews (AccountID, ProductID, ImgReview, Rating, Comment) VALUES (?, ?, ?, ?, ?);";

    public static List<Review> showReviewTrash(int page, int pageSize) {
        List<Review> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_TRASH);
            int offset = (page - 1) * pageSize; // Tính OFFSET dựa trên trang hiện tại
            stm.setInt(1, offset);
            stm.setInt(2, pageSize);
            rs = stm.executeQuery();
            while (rs.next()) {
                Review r = new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("AccountID"),
                        rs.getInt("ProductID"),
                        rs.getString("ImgReview"),
                        rs.getInt("IsDeleted"), // BIT có thể cần getBoolean
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getInt("Status"),
                        rs.getTimestamp("ReviewedAt"));
                list.add(r);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public static List<Review> showReview(int page, int pageSize) {
        List<Review> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_REVIEW);
            int offset = (page - 1) * pageSize; // Tính OFFSET dựa trên trang hiện tại
            stm.setInt(1, offset);
            stm.setInt(2, pageSize);
            rs = stm.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(2));
                Review r = new Review(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getInt(8),
                        rs.getTimestamp(9));
                list.add(r);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public static List<Review> showReviewForCustomer(int productId) {
        List<Review> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_REVIEW_FOR_CUSTOMER);
            stm.setInt(1, productId);
            rs = stm.executeQuery();
            while (rs.next()) {
                Review r = new Review(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getTimestamp(6));
                list.add(r);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public static boolean UpdateStatusReview(int ReviewID, int Status) {
        boolean updateRow = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(UPDATE_STATUS);
            stm.setInt(1, Status);
            stm.setInt(2, ReviewID);
            updateRow = stm.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return updateRow;
    }

    public static boolean UpdateStatusIsDeletedReview(int ReviewID, int IsDeleted) {
        boolean updateRow = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(UPDATE_ISDELETED);
            stm.setInt(1, IsDeleted);
            stm.setInt(2, ReviewID);
            updateRow = stm.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return updateRow;
    }

    public static boolean deleteReview(int reviewID) {
        boolean deleteaRow = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(DELETE_REVIEW_BY_ID);
            stm.setInt(1, reviewID);
            deleteaRow = stm.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return deleteaRow;
    }

    public static boolean addReview(Review review) {
        boolean rowUpdate = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(ADD_PREVIEW);
            stm.setInt(1, review.getAccountID());
            stm.setInt(2, review.getProductID());
            stm.setString(3, review.getImgReview());
            stm.setInt(4, review.getRating());
            stm.setString(5, review.getComment());
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowUpdate;
    }

    public static List<Review> searchReview(int filterRating, int filterStatus, int filterUserIDOrProductID, int offset, int limit) {
        List<Review> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM Reviews WHERE 1=1";
            List<Object> params = new ArrayList<>();

            if (filterUserIDOrProductID != 0) {
                sql += " AND (AccountID = ? OR ProductID = ?)";
                params.add(filterUserIDOrProductID);
                params.add(filterUserIDOrProductID);
            }
            if (filterRating != 0) {
                sql += " AND Rating = ?";
                params.add(filterRating);
            }
            if (filterStatus != -1) {
                sql += " AND Status = ?";
                params.add(filterStatus);
            }

            // Thêm phân trang với ORDER BY, OFFSET và FETCH NEXT
            sql += " ORDER BY ReviewID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            params.add(offset);
            params.add(limit);
            stm = conn.prepareStatement(sql);

            // Gán các tham số cho PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("AccountID"),
                        rs.getInt("ProductID"),
                        rs.getString("Comment"),
                        rs.getInt("Rating"),
                        rs.getString("ImgReview"),
                        rs.getInt("Status"),
                        rs.getTimestamp("ReviewedAt")
                );
                list.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

   public static List<Review> getReviewsFromDatabase(int star, int productId, int offset, int limit) 
        throws SQLException, ClassNotFoundException {
    List<Review> reviews = new ArrayList<>();
    String sql = "SELECT r.*, a.AccountName, a.Image " +
                 "FROM Reviews r " +
                 "JOIN Account a ON r.AccountID = a.AccountID " +
                 "WHERE r.ProductID = ? AND r.Status = 0 ";
    
    // Thêm điều kiện filter
    switch (star) {
        case 0:
            sql += "AND r.Comment IS NOT NULL AND r.Comment <> '' ";
            break;
        case -1:
            sql += "AND r.ImgReview IS NOT NULL AND r.ImgReview <> '' ";
            break;
        case 6:
            // Không thêm điều kiện
            break;
        default:
            sql += "AND r.Rating = ? ";
    }
    
    sql += "ORDER BY r.ReviewedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        // Gán tham số theo thứ tự cố định
        stmt.setInt(1, productId); // Tham số 1: ProductID
        
        // Nếu star từ 1-5, gán Rating vào tham số 2
        if (star >= 1 && star <= 5) {
            stmt.setInt(2, star);
            stmt.setInt(3, offset);  // Tham số 3: OFFSET
            stmt.setInt(4, limit);   // Tham số 4: FETCH NEXT
        } else {
            stmt.setInt(2, offset);  // Tham số 2: OFFSET
            stmt.setInt(3, limit);   // Tham số 3: FETCH NEXT
        }
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Review review = new Review(
                rs.getInt("ReviewID"),
                rs.getInt("AccountID"),
                rs.getInt("ProductID"),
                rs.getString("Comment"),
                rs.getInt("Rating"),
                rs.getString("ImgReview"),
                rs.getInt("Status"),
                rs.getTimestamp("ReviewedAt")
            ); 
            // Thêm UserName vào Review
            Account acc = new Account();
            acc.setUserName(rs.getString("AccountName"));
            acc.setImage(rs.getString("Image"));
            review.setAccount(acc);
            reviews.add(review);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
  
    return reviews;
}
public static int getTotalReviews(int star, int productId) throws SQLException, ClassNotFoundException {
    String sql = "SELECT COUNT(*) FROM Reviews WHERE ProductID = ? AND Status = 0 ";
    
    switch (star) {
        case 0:
            sql += "AND Comment IS NOT NULL AND Comment <> ''";
            break;
        case -1:
            sql += "AND ImgReview IS NOT NULL AND ImgReview <> ''";
            break;
        case 6:
            break;
        default:
            sql += "AND Rating = ?";
    }
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, productId);
        if (star >= 1 && star <= 5) {
            stmt.setInt(2, star);
        }
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}
}
