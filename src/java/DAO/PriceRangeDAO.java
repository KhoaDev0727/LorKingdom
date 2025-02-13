/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.PriceRange;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class PriceRangeDAO {
    // Lấy danh sách tất cả khoảng giá

    public List<PriceRange> getAllPriceRanges() throws SQLException, ClassNotFoundException {
        List<PriceRange> priceRanges = new ArrayList<>();
        String query = "SELECT PriceRangeID, PriceRange, CreatedAt FROM PriceRange";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                priceRanges.add(new PriceRange(
                        rs.getInt("PriceRangeID"),
                        rs.getString("PriceRange"),
                        rs.getTimestamp("CreatedAt")
                ));
            }
        }
        return priceRanges;
    }

    // Thêm khoảng giá mới
    public void addPriceRange(PriceRange priceRange) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO PriceRange (PriceRange) VALUES (?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, priceRange.getPriceRange());
            ps.executeUpdate();
        }
    }

    // Kiểm tra khoảng giá đã tồn tại hay chưa
    public boolean isPriceRangeExists(String priceRange) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM PriceRange WHERE PriceRange = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, priceRange);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Cập nhật khoảng giá
    public void updatePriceRange(PriceRange priceRange) throws SQLException, ClassNotFoundException {
        String query = "UPDATE PriceRange SET PriceRange = ? WHERE PriceRangeID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, priceRange.getPriceRange());
            ps.setInt(2, priceRange.getPriceRangeID());
            ps.executeUpdate();
        }
    }

    // Xóa khoảng giá theo ID
    public void deletePriceRange(int priceRangeID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM PriceRange WHERE PriceRangeID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, priceRangeID);
            ps.executeUpdate();
        }
    }
}
