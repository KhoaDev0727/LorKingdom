/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Category;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class CategoryDAO {

    public List<Category> getActiveCategoriesWithProducts() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();

        // Truy vấn:
        // 1) Lọc Category chưa xóa (c.IsDeleted=0)
        // 2) Chỉ lấy Category có ít nhất 1 Product (p) thỏa mãn p.IsDeleted=0 (và p.Quantity>0 nếu cần)
        String query
                = "SELECT c.CategoryID, c.SuperCategoryID, c.Name, c.CreatedAt, c.IsDeleted "
                + "FROM Category c "
                + "WHERE c.IsDeleted = 0 "
                + "  AND EXISTS ( "
                + "      SELECT 1 "
                + "      FROM Product p "
                + "      WHERE p.CategoryID = c.CategoryID "
                + "        AND p.IsDeleted = 0 "
                + "        AND p.Quantity > 0 "
                + // Bỏ dòng này nếu không muốn kiểm tra số lượng còn hàng
                "  )";

        try (
                 Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("CategoryID"),
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                );
                categories.add(category);
            }
        }

        return categories;
    }

    public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT CategoryID, SuperCategoryID, Name, CreatedAt, IsDeleted FROM Category Where IsDeleted = 0 ";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return categories;
    }

    public List<Category> getAcctiveCategories() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT CategoryID, SuperCategoryID, Name, CreatedAt, IsDeleted FROM Category Where IsDeleted = 1 ";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return categories;
    }

    public void addCategory(Category category) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Category (SuperCategoryID, Name, CreatedAt) VALUES (?, ?, GETDATE())";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, category.getSuperCategoryID());
            ps.setString(2, category.getName());
            ps.executeUpdate();
        }
    }

    public void hardDeleteCategory(int categoryID) throws SQLException, ClassNotFoundException {
        String query = "Delete FROM Category WHERE CategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryID);
            ps.executeUpdate();
        }
    }

    public void deleteCategory(int categoryID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Category SET IsDeleted = 1 WHERE CategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryID);
            ps.executeUpdate();
        }
    }

    public List<Category> searchCategory(String keyword) throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT CategoryID, SuperCategoryID, Name, CreatedAt, IsDeleted FROM Category WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("CategoryID"),
                            rs.getInt("SuperCategoryID"),
                            rs.getString("Name"),
                            rs.getDate("CreatedAt"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return categories;
    }

    public boolean isCategoryExists(String name, int categoryID) throws SQLException, ClassNotFoundException {
        String query;
        if (categoryID <= 0) {
            query = "SELECT COUNT(*) FROM Category WHERE Name = ?";
        } else {
            query = "SELECT COUNT(*) FROM Category WHERE Name = ? AND CategoryID <> ?";
        }

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            if (categoryID > 0) {
                ps.setInt(2, categoryID);
            }
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public String getCategoryNameByProductId(int productId) throws SQLException, ClassNotFoundException {
        String categoryName = null;
        String sql = "SELECT c.Name FROM Product p JOIN Category c ON p.CategoryID = c.CategoryID WHERE p.ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoryName = rs.getString("Name");
                }
            }
        }
        return categoryName;
    }

    public void updateCategory(Category category) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Category SET SuperCategoryID = ?, Name = ? WHERE CategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, category.getSuperCategoryID());
            ps.setString(2, category.getName());
            ps.setInt(3, category.getCategoryID());
            ps.executeUpdate();
        }
    }

    public void restoreCategory(int categoryID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Category SET IsDeleted = 0 WHERE CategoryID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryID);
            ps.executeUpdate();
        }
    }

    public int getTotalCategoriesCount() throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Category WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Category> getCategoriesByPage(int page, int pageSize) throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String query = "SELECT CategoryID, SuperCategoryID, Name, CreatedAt, IsDeleted "
                + "FROM Category "
                + "WHERE IsDeleted = 0 "
                + "ORDER BY CreatedAt ASC  "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("CategoryID"),
                            rs.getInt("SuperCategoryID"),
                            rs.getString("Name"),
                            rs.getDate("CreatedAt"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return categories;
    }

    public List<Category> getActiveCategoriesWithActiveSuperCategory() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT c.CategoryID, c.SuperCategoryID, c.Name, c.CreatedAt, c.IsDeleted "
                + "FROM Category c "
                + "JOIN SuperCategory sc ON c.SuperCategoryID = sc.SuperCategoryID "
                + "WHERE c.IsDeleted = 0 AND sc.IsDeleted = 0";

        try (
                 Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("CategoryID"),
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                );
                categories.add(category);
            }
        }

        return categories;
    }

}
