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

    public List<Origin> getAllActiveOrigins() throws SQLException, ClassNotFoundException {
        List<Origin> list = new ArrayList<>();
        String query = "SELECT OriginID, Name, CreatedAt, IsDeleted "
                + "FROM Origin "
                + "WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Origin origin = new Origin(
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                );
                list.add(origin);
            }
        }
        return list;
    }

    // 2) Lấy danh sách Origin đã xóa mềm (isDeleted=1)
    public List<Origin> getDeletedOrigins() throws SQLException, ClassNotFoundException {
        List<Origin> list = new ArrayList<>();
        String query = "SELECT OriginID, Name, CreatedAt, IsDeleted "
                + "FROM Origin "
                + "WHERE IsDeleted = 1";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Origin origin = new Origin(
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                );
                list.add(origin);
            }
        }
        return list;
    }

    public void addOrigin(Origin origin) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Origin (Name) VALUES (?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, origin.getName());
            ps.executeUpdate();
        }
    }

    public void updateOrigin(Origin origin) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Origin SET Name = ? WHERE OriginID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, origin.getName());
            ps.setInt(2, origin.getOriginID());
            ps.executeUpdate();
        }
    }

    // 4) Xóa mềm (IsDeleted=1)
    public void softDeleteOrigin(int originID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Origin SET IsDeleted = 1 WHERE OriginID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, originID);
            ps.executeUpdate();
        }
    }

    // 5) Xóa cứng (DELETE FROM DB)
    public void hardDeleteOrigin(int originID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Origin WHERE OriginID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, originID);
            ps.executeUpdate();
        }
    }

    public void restoreOrigin(int originID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Origin SET IsDeleted = 0 WHERE OriginID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, originID);
            ps.executeUpdate();
        }
    }

    public List<Origin> searchOrigin(String keyword) throws SQLException, ClassNotFoundException {
        List<Origin> origins = new ArrayList<>();
        String query = "SELECT OriginID, Name, CreatedAt, IsDeleted FROM Origin WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    origins.add(new Origin(
                            rs.getInt("OriginID"),
                            rs.getString("Name"),
                            rs.getDate("CreatedAt"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return origins;
    }

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

    public String getOriginNameByProductId(int productId) throws SQLException, ClassNotFoundException {
        String originName = null;
        String sql = "SELECT o.Name FROM Product p JOIN Origin o ON p.OriginID = o.OriginID WHERE p.ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    originName = rs.getString("Name");
                }
            }
        }
        return originName;
    }

}
