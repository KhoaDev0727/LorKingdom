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

/**
 *
 * @author admin1
 */
public class BrandDAO {

    public List<Brand> getAllBrands() throws SQLException, ClassNotFoundException {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT BrandID, BrandName, OriginBrand, CreatedAt FROM Brand"; // Cập nhật truy vấn

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                brands.add(new Brand(
                        rs.getInt("BrandID"),
                        rs.getString("BrandName"),
                        rs.getString("OriginBrand"), // Lấy giá trị từ cột OriginBrand
                        rs.getDate("CreatedAt")
                ));
            }
        }
        return brands;
    }

    public void addBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Brand (BrandName, OriginBrand) VALUES (?, ?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getOriginBrand());  // Thêm OriginBrand vào câu lệnh SQL
            ps.executeUpdate();
        }
    }

    public void deleteBrand(int brandID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Brand WHERE BrandID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, brandID);
            ps.executeUpdate();
        }
    }

    public List<Brand> searchBrand(String keyword) throws SQLException, ClassNotFoundException {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT BrandID, BrandName, OriginBrand, CreatedAt FROM Brand WHERE BrandName LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    brands.add(new Brand(
                            rs.getInt("BrandID"),
                            rs.getString("BrandName"),
                            rs.getString("OriginBrand"), // Lấy giá trị từ cột OriginBrand
                            rs.getDate("CreatedAt")
                    ));
                }
            }
        }
        return brands;
    }

    public void updateBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Brand SET BrandName = ?, OriginBrand = ? WHERE BrandID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getOriginBrand());  // Thêm OriginBrand vào câu lệnh SQL
            ps.setInt(3, brand.getBrandID());
            ps.executeUpdate();
        }
    }

    public boolean isBrandExists(String brandName) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Brand WHERE BrandName = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, brandName);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
