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

    public List<PriceRange> getAllActivePriceRanges() throws SQLException, ClassNotFoundException {
        List<PriceRange> list = new ArrayList<>();
        String query = "SELECT PriceRangeID, PriceRange, CreatedAt, IsDeleted "
                + "FROM PriceRange "
                + "WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PriceRange pr = new PriceRange(
                        rs.getInt("PriceRangeID"),
                        rs.getString("PriceRange"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                );
                list.add(pr);
            }
        }
        return list;
    }

    // 2) Lấy tất cả PriceRange đã xóa mềm (isDeleted=1)
    public List<PriceRange> getDeletedPriceRanges() throws SQLException, ClassNotFoundException {
        List<PriceRange> list = new ArrayList<>();
        String query = "SELECT PriceRangeID, PriceRange, CreatedAt, IsDeleted "
                + "FROM PriceRange "
                + "WHERE IsDeleted = 1";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PriceRange pr = new PriceRange(
                        rs.getInt("PriceRangeID"),
                        rs.getString("PriceRange"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                );
                list.add(pr);
            }
        }
        return list;
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

    // 4) Xóa mềm
    public void softDeletePriceRange(int priceRangeID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE PriceRange SET IsDeleted = 1 WHERE PriceRangeID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, priceRangeID);
            ps.executeUpdate();
        }
    }

    // 5) Xóa cứng
    public void hardDeletePriceRange(int priceRangeID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM PriceRange WHERE PriceRangeID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, priceRangeID);
            ps.executeUpdate();
        }
    }

    // 6) Khôi phục
    public void restorePriceRange(int priceRangeID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE PriceRange SET IsDeleted = 0 WHERE PriceRangeID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, priceRangeID);
            ps.executeUpdate();
        }
    }

    public List<PriceRange> searchPriceRange(String keyword) throws SQLException, ClassNotFoundException {
        List<PriceRange> priceRanges = new ArrayList<>();
        String query = "SELECT PriceRangeID, PriceRange, CreatedAt, IsDeleted FROM PriceRange WHERE PriceRange LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    priceRanges.add(new PriceRange(
                            rs.getInt("PriceRangeID"),
                            rs.getString("PriceRange"),
                            rs.getDate("CreatedAt"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return priceRanges;
    }

}
