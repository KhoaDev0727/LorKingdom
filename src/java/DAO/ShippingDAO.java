package DAO;

import DBConnect.DBConnection;
import Model.Shipping;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShippingDAO {

    public List<Shipping> getAllShippingMethods() throws SQLException, ClassNotFoundException {
        List<Shipping> shippingMethods = new ArrayList<>();
        String sql = "SELECT * FROM ShippingMethod";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                shippingMethods.add(new Shipping(
                        rs.getInt("ShippingMethodID"),
                        rs.getString("MethodName"),
                        rs.getBigDecimal("Price"),
                        rs.getString("Description")
                ));
            }
        }
        return shippingMethods;
    }

    public void addShippingMethod(String methodName, BigDecimal price, String description) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ShippingMethod (MethodName, Price, Description) VALUES (?, ?, ?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, methodName);
            stmt.setBigDecimal(2, price);
            stmt.setString(3, description);
            stmt.executeUpdate();
        }
    }

    public void updateShippingMethod(int id, String methodName, BigDecimal price, String description) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ShippingMethod SET MethodName = ?, Price = ?, Description = ? WHERE ShippingMethodID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, methodName);
            stmt.setBigDecimal(2, price);
            stmt.setString(3, description);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public void deleteShippingMethod(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ShippingMethod WHERE ShippingMethodID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Shipping> searchShippingMethods(String keyword) throws SQLException, ClassNotFoundException {
        List<Shipping> shippingMethods = new ArrayList<>();
        String sql = "SELECT * FROM ShippingMethod WHERE LOWER(MethodName) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shippingMethods.add(new Shipping(
                            rs.getInt("ShippingMethodID"),
                            rs.getString("MethodName"),
                            rs.getBigDecimal("Price"),
                            rs.getString("Description")
                    ));
                }
            }
        }
        return shippingMethods;
    }

    public boolean isShippingMethodExists(String methodName) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM ShippingMethod WHERE MethodName = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, methodName);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
