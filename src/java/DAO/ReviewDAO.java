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

    private static String SELECT_REVIEW = "SELECT * FROM Reviews";
    private static String UPDATE_STATUS = "UPDATE Reviews SET Status = ? WHERE ReviewID = ?;";
    private static String DELETE_REVIEW_BY_ID = "DELETE FROM Reviews WHERE ReviewID = ?";

    public static List<Review> showReview() {
        List<Review> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_REVIEW);
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

}
