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
        String query = "SELECT BrandID, Name, OriginBrand, CreatedAt FROM Brand";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                Brand brand = new Brand(
                        rs.getInt("BrandID"),
                        rs.getString("Name"),      // Lấy giá trị từ cột Name, mapping sang brandName
                        rs.getString("OriginBrand"),
                        rs.getDate("CreatedAt")
                );
                brands.add(brand);
            }
        }
        return brands;
    }

    // Thêm một Brand mới
    public void addBrand(Brand brand) throws SQLException, ClassNotFoundException {
        // Vì cột CreatedAt có giá trị mặc định GETDATE() nên ta không cần set giá trị này
        String query = "INSERT INTO Brand (Name, OriginBrand) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getOriginBrand());
            ps.executeUpdate();
        }
    }

    // Xoá Brand theo BrandID
    public void deleteBrand(int brandID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Brand WHERE BrandID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setInt(1, brandID);
            ps.executeUpdate();
        }
    }

    // Tìm kiếm Brand theo từ khóa (tìm theo tên)
    public List<Brand> searchBrand(String keyword) throws SQLException, ClassNotFoundException {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT BrandID, Name, OriginBrand, CreatedAt FROM Brand WHERE Name LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Brand brand = new Brand(
                            rs.getInt("BrandID"),
                            rs.getString("Name"),
                            rs.getString("OriginBrand"),
                            rs.getDate("CreatedAt")
                    );
                    brands.add(brand);
                }
            }
        }
        return brands;
    }
    
    // Kiểm tra xem Brand có tồn tại theo tên hay không
    public boolean isBrandExists(String brandName) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Brand WHERE Name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setString(1, brandName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // Cập nhật thông tin của Brand
    public void updateBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Brand SET Name = ?, OriginBrand = ? WHERE BrandID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getOriginBrand());
            ps.setInt(3, brand.getBrandID());
            ps.executeUpdate();
        }
    }
}
