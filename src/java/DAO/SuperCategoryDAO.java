package DAO;

import DBConnect.DBConnection;
import Model.SuperCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuperCategoryDAO {
// Lấy danh sách tất cả danh mục cha

    public List<SuperCategory> getAllSuperCategories() throws SQLException, ClassNotFoundException {
        List<SuperCategory> categories = new ArrayList<>();
        String query = "SELECT SuperCategoryID, Name, CreatedAt FROM SuperCategory";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(new SuperCategory(
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        new Date(rs.getTimestamp("CreatedAt").getTime())
                ));
            }
        }
        return categories;
    }

    // Thêm danh mục mới
    public void addSuperCategory(SuperCategory category) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO SuperCategory (Name, CreatedAt) VALUES (?, GETDATE())";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
        }
    }

    // Xóa danh mục theo ID
    public void deleteSuperCategory(int superCategoryID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM SuperCategory WHERE SuperCategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, superCategoryID);
            ps.executeUpdate();
        }
    }

    // Tìm kiếm danh mục theo từ khóa
    public List<SuperCategory> searchSuperCategory(String keyword) throws SQLException, ClassNotFoundException {
        List<SuperCategory> categories = new ArrayList<>();
        String query = "SELECT SuperCategoryID, Name, CreatedAt FROM SuperCategory WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(new SuperCategory(
                            rs.getInt("SuperCategoryID"),
                            rs.getString("Name"),
                            rs.getTimestamp("CreatedAt")
                    ));
                }
            }
        }
        return categories;
    }

    // Cập nhật danh mục cha (chỉ cập nhật tên, không có UpdatedAt)
    public void updateSuperCategory(int superCategoryID, String newName) throws SQLException, ClassNotFoundException {
        String query = "UPDATE SuperCategory SET Name = ? WHERE SuperCategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newName);
            ps.setInt(2, superCategoryID);
            ps.executeUpdate();
        }
    }

    // Kiểm tra xem danh mục đã tồn tại hay chưa
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
}
