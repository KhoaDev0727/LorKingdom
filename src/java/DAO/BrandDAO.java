/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Brand;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {

    // Lấy danh sách tất cả các Brand
    public List<Brand> getAllBrands() throws SQLException, ClassNotFoundException {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT BrandID, Name, CreatedAt FROM Brand";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                brands.add(new Brand(
                        rs.getInt("BrandID"),
                        rs.getString("Name"),
                        rs.getDate("CreatedAt")
                ));
            }
        }
        return brands;
    }

    // Thêm mới Brand
    public void addBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Brand (Name) VALUES (?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, brand.getName());
            ps.executeUpdate();
        }
    }

    // Cập nhật Brand
    public void updateBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Brand SET Name = ? WHERE BrandID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, brand.getName());
            ps.setInt(2, brand.getBrandID());
            ps.executeUpdate();
        }
    }

    // Xóa Brand theo ID
    public void deleteBrand(int brandID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Brand WHERE BrandID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, brandID);
            ps.executeUpdate();
        }
    }

    // Tìm kiếm Brand theo từ khóa
    public List<Brand> searchBrand(String keyword) throws SQLException, ClassNotFoundException {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT BrandID, Name, CreatedAt FROM Brand WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    brands.add(new Brand(
                            rs.getInt("BrandID"),
                            rs.getString("Name"),
                            rs.getDate("CreatedAt")
                    ));
                }
            }
        }
        return brands;
    }

    // Kiểm tra xem Brand đã tồn tại hay chưa
    public boolean isBrandExists(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Brand WHERE Name = ?";
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

    public String getBrandNameByProductId(int productId) throws SQLException, ClassNotFoundException {
        String brandName = null;
        String sql = "SELECT b.Name FROM Product p JOIN Brand b ON p.BrandID = b.BrandID WHERE p.ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    brandName = rs.getString("Name");
                }
            }
        }
        return brandName;
    }

}
