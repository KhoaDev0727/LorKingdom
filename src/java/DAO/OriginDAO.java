/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Origin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OriginDAO {

    // Lấy danh sách tất cả Origin
    public List<Origin> getAllOrigins() throws SQLException, ClassNotFoundException {
        List<Origin> origins = new ArrayList<>();
        String query = "SELECT OriginID, Name, CreatedAt FROM Origin";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Origin origin = new Origin(
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt")
                );
                origins.add(origin);
            }
        }
        return origins;
    }

    // Thêm mới Origin
    public void addOrigin(Origin origin) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Origin (Name) VALUES (?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, origin.getName());
            ps.executeUpdate();
        }
    }

    // Cập nhật Origin
    public void updateOrigin(Origin origin) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Origin SET Name = ? WHERE OriginID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, origin.getName());
            ps.setInt(2, origin.getOriginID());
            ps.executeUpdate();
        }
    }

    // Xóa Origin theo OriginID
    public void deleteOrigin(int originID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Origin WHERE OriginID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, originID);
            ps.executeUpdate();
        }
    }

    // Tìm kiếm Origin theo từ khóa (dựa trên tên)
    public List<Origin> searchOrigin(String keyword) throws SQLException, ClassNotFoundException {
        List<Origin> origins = new ArrayList<>();
        String query = "SELECT OriginID, Name, CreatedAt FROM Origin WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Origin origin = new Origin(
                            rs.getInt("OriginID"),
                            rs.getString("Name"),
                            rs.getDate("CreatedAt")
                    );
                    origins.add(origin);
                }
            }
        }
        return origins;
    }

    // Kiểm tra xem Origin đã tồn tại hay chưa (theo tên)
    public boolean isOriginExists(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Origin WHERE Name = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
