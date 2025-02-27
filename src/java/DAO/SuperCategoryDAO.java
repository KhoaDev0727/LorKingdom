
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.SuperCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class SuperCategoryDAO {

    public List<SuperCategory> getAllSuperCategories() throws SQLException, ClassNotFoundException {
        List<SuperCategory> categories = new ArrayList<>();
        String query = "SELECT SuperCategoryID, Name, CreatedAt, IsDeleted FROM SuperCategory";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new SuperCategory(
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        rs.getTimestamp("CreatedAt"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return categories;
    }

    public List<SuperCategory> getActiveSuperCategories() throws SQLException, ClassNotFoundException {
        List<SuperCategory> categories = new ArrayList<>();
        String query = "SELECT SuperCategoryID, Name, CreatedAt, IsDeleted FROM SuperCategory WHERE IsDeleted = 0";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new SuperCategory(
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        rs.getTimestamp("CreatedAt"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return categories;
    }

    public void addSuperCategory(SuperCategory category) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO SuperCategory (Name, CreatedAt) VALUES (?, GETDATE())";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
        }
    }

    // Cập nhật để thực hiện xóa mềm
    public void deleteSuperCategory(int superCategoryID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE SuperCategory SET IsDeleted = 1 WHERE SuperCategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, superCategoryID);
            ps.executeUpdate();
        }
    }

    public List<SuperCategory> searchSuperCategory(String keyword) throws SQLException, ClassNotFoundException {
        List<SuperCategory> categories = new ArrayList<>();
        String query = "SELECT SuperCategoryID, Name, CreatedAt, IsDeleted FROM SuperCategory WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(new SuperCategory(
                            rs.getInt("SuperCategoryID"),
                            rs.getString("Name"),
                            rs.getTimestamp("CreatedAt"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return categories;
    }

    public void updateSuperCategory(int superCategoryID, String newName) throws SQLException, ClassNotFoundException {
        String query = "UPDATE SuperCategory SET Name = ? WHERE SuperCategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newName);
            ps.setInt(2, superCategoryID);
            ps.executeUpdate();
        }
    }

    public boolean isSuperCategoryExists(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM SuperCategory WHERE Name = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try ( ResultSet res = ps.executeQuery()) {
                if (res.next()) {
                    return res.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void restoreSuperCategory(int superCategoryID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE SuperCategory SET IsDeleted = 0 WHERE SuperCategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, superCategoryID);
            ps.executeUpdate();
        }
    }

}
