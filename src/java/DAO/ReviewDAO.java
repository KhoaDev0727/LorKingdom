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

    private static String SELECT_REVIEW = "SELECT * FROM Review";

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

}
