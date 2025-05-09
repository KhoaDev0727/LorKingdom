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

    public List<Material> getAllActiveMaterials() throws SQLException, ClassNotFoundException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT MaterialID, Name, Description, IsDeleted "
                + "FROM Material "
                + "WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Material mat = new Material(
                        rs.getInt("MaterialID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                );
                materials.add(mat);
            }
        }
        return materials;
    }

    // 2) Lấy danh sách Material đã xóa mềm (isDeleted=1)
    public List<Material> getDeletedMaterials() throws SQLException, ClassNotFoundException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT MaterialID, Name, Description, IsDeleted "
                + "FROM Material "
                + "WHERE IsDeleted = 1";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Material mat = new Material(
                        rs.getInt("MaterialID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                );
                materials.add(mat);
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

    public boolean isMaterialExists(String name, int ignoreID) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Material WHERE Name = ? AND MaterialID <> ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, ignoreID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Nếu có >= 1 record khác trùng tên thì trả về true
                    return rs.getInt(1) >= 1;
                }
            }
        }
        return false;
    }

    // Cập nhật thông tin vật liệu
    public void updateMaterial(Material material) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Material SET Name = ?, Description = ?, IsDeleted = ? WHERE MaterialID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, material.getName());
            ps.setString(2, material.getDescription());
            ps.setBoolean(3, material.getIsDeleted() == 1);
            ps.setInt(4, material.getMaterialID());
            ps.executeUpdate();
        }
    }

    public void softDeleteMaterial(int materialID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Material SET IsDeleted = 1 WHERE MaterialID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, materialID);
            ps.executeUpdate();
        }
    }

    // 5) Xóa cứng (DELETE)
    public void hardDeleteMaterial(int materialID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Material WHERE MaterialID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, materialID);
            ps.executeUpdate();
        }
    }

    // 6) Khôi phục (IsDeleted=0)
    public void restoreMaterial(int materialID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Material SET IsDeleted = 0 WHERE MaterialID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, materialID);
            ps.executeUpdate();
        }
    }

    public String getMaterialNameByProductId(int productId) throws SQLException, ClassNotFoundException {
        String materialName = null;
        String sql = "SELECT m.Name FROM Product p JOIN Material m ON p.MaterialID = m.MaterialID WHERE p.ProductID = ? ";
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
