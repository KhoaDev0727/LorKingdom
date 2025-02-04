package DAO;

import DBConnect.DBConnection;
import Model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho quản lý danh mục
 */
public class CategoryDAO {

    // Lấy danh sách tất cả danh mục
    public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT CategoryID, Name, CreatedAt FROM Category"; // Chỉ lấy các cột cần thiết

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getString("CreatedAt")
                ));
            }
        }
        return categories;
    }

    // Thêm danh mục mới
    public void addCategory(Category category) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Category (Name) VALUES (?)"; // Bỏ CreatedAt nếu cột này có giá trị mặc định

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
        }
    }

    // Xóa danh mục theo ID
    public void deleteCategory(int categoryID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Category WHERE CategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryID);
            ps.executeUpdate();
        }
    }

    // Tìm kiếm danh mục theo từ khóa
    public List<Category> searchCategory(String keyword) throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT CategoryID, Name, CreatedAt FROM Category WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("Name"),
                            rs.getString("CreatedAt")
                    ));
                }
            }
        }
        return categories;
    }

    // Cập nhật danh mục
    public void updateCategory(Category category) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Category SET Name = ? WHERE CategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getCategoryID());
            ps.executeUpdate();
        }
    }

    public boolean isCategoryExists(String name) throws SQLException, ClassNotFoundException {
        String query = "Select count(*) FROM Category WHERE Name =?";
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
