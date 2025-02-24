package DAO;

import DBConnect.DBConnection;
import Model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    public void addPaymentMethod(String methodName, String description) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO PaymentMethods (MethodName, Description) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, methodName);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }
    }
    
    public List<Payment> getAllPaymentMethods() throws SQLException, ClassNotFoundException {
        List<Payment> paymentMethods = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethods";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                paymentMethods.add(new Payment(
                    rs.getInt("PaymentMethodID"),
                    rs.getString("MethodName"),
                    rs.getString("Description")
                ));
            }
        }
        return paymentMethods;
    }
    
    public void updatePaymentMethod(int id, String methodName, String description) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE PaymentMethods SET MethodName = ?, Description = ? WHERE PaymentMethodID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, methodName);
            stmt.setString(2, description);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }
    
    public void deletePaymentMethod(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM PaymentMethods WHERE PaymentMethodID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Payment> searchPaymentMethods(String keyword) throws SQLException, ClassNotFoundException {
        List<Payment> paymentMethods = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethods WHERE LOWER(MethodName) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paymentMethods.add(new Payment(
                        rs.getInt("PaymentMethodID"),
                        rs.getString("MethodName"),
                        rs.getString("Description")
                    ));
                }
            }
        }
        return paymentMethods;
    }
    public boolean isPaymentMethodExists(String methodName) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM PaymentMethods WHERE MethodName = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, methodName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
