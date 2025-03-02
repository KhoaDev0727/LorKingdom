/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
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

    private static String SELECT_REVIEW = "SELECT * FROM Reviews ORDER BY ReviewID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
    private static String SELECT_REVIEW_FOR_CUSTOMER = "SELECT AccountID, ProductID, ImgReview,Rating, Comment,ReviewedAt FROM Reviews WHERE ProductID = ? AND Status = 0";
    private static String UPDATE_STATUS = "UPDATE Reviews SET Status = ? WHERE ReviewID = ?;";
    private static String DELETE_REVIEW_BY_ID = "DELETE FROM Reviews WHERE ReviewID = ?";
    private static String ADD_PREVIEW = "INSERT INTO Reviews (AccountID, ProductID, ImgReview, Rating, Comment) VALUES (?, ?, ?, ?, ?);";

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
                Review r = new Review(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getTimestamp(8));
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

//    Search Review OF Custommer
    public static List<Review> searchReviewsByRatingAndContent(int productId, Integer rating, Boolean hasComment, Boolean hasImage) throws SQLException, ClassNotFoundException {
        List<Review> result = new ArrayList<>();
        conn = DBConnection.getConnection();
        StringBuilder sql = new StringBuilder("SELECT * FROM Reviews WHERE productID = ?");

        if (rating != null) {
            sql.append(" AND rating = ?");
        }
        if (hasComment != null && hasComment) {
            sql.append(" AND comment IS NOT NULL AND comment != ''");
        }
        if (hasImage != null && hasImage) {
            sql.append(" AND imgReview IS NOT NULL AND imgReview != ''");
        }
        try {
            stm = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            stm.setInt(paramIndex++, productId);
            if (rating != null) {
                stm.setInt(paramIndex++, rating);
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                Review review = new Review();
                review.setAccountID(rs.getInt("AccountID"));
                review.setComment(rs.getString("Comment"));
                review.setRating(rs.getInt("Rating"));
                review.setReviewAt(rs.getTimestamp("ReviewAt"));
                review.setImgReview(rs.getString("ImgReview"));
                result.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
