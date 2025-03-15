package DAO;

import DBConnect.DBConnection;
import Model.Shipping;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShippingDAO {

    public List<Shipping> getAllShippingMethods() throws SQLException, ClassNotFoundException {
        List<Shipping> shippingMethods = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM ShippingMethods "
                + "WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                shippingMethods.add(new Shipping(
                        rs.getInt("ShippingMethodID"),
                        rs.getString("MethodName"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return shippingMethods;
    }

    public List<Shipping> getDeletedShippingMethod() throws SQLException, ClassNotFoundException {
        List<Shipping> shippingMethods = new ArrayList<>();
        String query = "SELECT ShippingMethodID, MethodName, Description, IsDeleted "
                + "FROM ShippingMethods "
                + "WHERE IsDeleted = 1";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Shipping method = new Shipping(
                        rs.getInt("ShippingMethodID"),
                        rs.getString("MethodName"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                );
                shippingMethods.add(method);
            }
        }
        return shippingMethods;
    }

    public void addShippingMethod(String methodName, String description) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ShippingMethods (MethodName, Description) VALUES (?, ?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, methodName);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }
    }

    public void updateShippingMethod(int id, String methodName, String description) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ShippingMethods SET MethodName = ?, Description = ? WHERE ShippingMethodID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, methodName);
            stmt.setString(2, description);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public void softDeleteShippingMethod(int ShippingMethodID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE ShippingMethods SET IsDeleted = 1 WHERE ShippingMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ShippingMethodID);
            ps.executeUpdate();
        }
    }

    // 5) Xóa cứng (DELETE)
    public void hardDeleteShippingMethod(int ShippingMethodID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM ShippingMethods WHERE ShippingMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ShippingMethodID);
            ps.executeUpdate();
        }
    }

    // 6) Khôi phục (IsDeleted=0)
    public void restoreShippingMethod(int ShippingMethodID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE ShippingMethods SET IsDeleted = 0 WHERE ShippingMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ShippingMethodID);
            ps.executeUpdate();
        }
    }

    public boolean deleteShippingMethod(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ShippingMethods WHERE ShippingMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No shipping method deleted - ID " + id + " not found or error!");
                return false;
            }
            System.out.println("Successfully deleted shipping method with ID: " + id);
            return true;
        }
    }

    public List<Shipping> searchShippingMethods(String keyword) throws SQLException, ClassNotFoundException {
        List<Shipping> shippingMethods = new ArrayList<>();
        String sql = "SELECT * FROM ShippingMethods WHERE LOWER(MethodName) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shippingMethods.add(new Shipping(
                            rs.getInt("ShippingMethodID"),
                            rs.getString("MethodName"),
                            rs.getString("Description"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return shippingMethods;
    }

    public boolean isShippingMethodExists(String methodName, int excludeID)
            throws SQLException, ClassNotFoundException {
        // Thêm điều kiện ShippingMethodID <> ? để bỏ qua bản ghi hiện tại
        String query = "SELECT COUNT(*) FROM ShippingMethods "
                + "WHERE MethodName = ? AND ShippingMethodID <> ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, methodName);
            ps.setInt(2, excludeID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
