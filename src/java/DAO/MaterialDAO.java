package DAO;

import DBConnect.DBConnection;
import Model.Material;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public List<Material> getAllMaterials() throws SQLException, ClassNotFoundException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT MaterialID, Name, Description, IsDeleted FROM Material";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                materials.add(new Material(
                        rs.getInt("MaterialID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return materials;
    }

    public List<Material> getActiveMaterials() throws SQLException, ClassNotFoundException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT MaterialID, Name, Description, IsDeleted FROM Material WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                materials.add(new Material(
                        rs.getInt("MaterialID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return materials;
    }

    // Thêm vật liệu mới
    public void addMaterial(Material material) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Material (Name, Description) VALUES (?, ?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, material.getName());
            ps.setString(2, material.getDescription());
            ps.executeUpdate();
        }
    }

    public boolean isMaterial(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Material WHERE Name = ?";
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

    // Cập nhật thông tin vật liệu
    public void updateMaterial(Material material) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Material SET Name = ?, Description = ? WHERE MaterialID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, material.getName());
            ps.setString(2, material.getDescription());
            ps.setInt(3, material.getMaterialID());
            ps.executeUpdate();
        }
    }

    // Xóa vật liệu theo ID
//    public void deleteMaterial(int materialID) throws SQLException, ClassNotFoundException {
//        String query = "DELETE FROM Material WHERE MaterialID = ?";
//
//        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
//
//            System.out.println("Deleting Material ID: " + materialID);
//            ps.setInt(1, materialID);
//            int rowsAffected = ps.executeUpdate();
//
//            if (rowsAffected == 0) {
//                System.out.println("No rows deleted. Material ID may not exist.");
//            }
//        }
//    }
    public void deleteMaterial(int materialID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Material SET IsDeleted = 1 WHERE MaterialID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, materialID);
            ps.executeUpdate();
        }
    }

    public void restoreMaterial(int materialID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Material SET IsDeleted = 0 WHERE MaterialID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, materialID);
            ps.executeUpdate();
        }
    }

    public String getMaterialNameByProductId(int productId) throws SQLException, ClassNotFoundException {
        String materialName = null;
        String sql = "SELECT m.Name FROM Product p JOIN Material m ON p.MaterialID = m.MaterialID WHERE p.ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    materialName = rs.getString("Name");
                }
            }
        }
        return materialName;
    }

    public List<Material> searchMaterial(String keyword) throws SQLException, ClassNotFoundException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT MaterialID, Name, Description, IsDeleted FROM Material WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materials.add(new Material(
                            rs.getInt("MaterialID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return materials;
    }

}
