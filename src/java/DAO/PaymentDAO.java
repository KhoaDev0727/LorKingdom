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
        String sql = "SELECT * "
        + "FROM PaymentMethods "
        + "WHERE IsDeleted = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                paymentMethods.add(new Payment(
                    rs.getInt("PaymentMethodID"),
                    rs.getString("MethodName"),
                    rs.getString("Description"),
                    rs.getInt("IsDeleted")
                ));
            }
        }
        return paymentMethods;
    }
    
    public List<Payment> getDeletedPaymentMethod() throws SQLException, ClassNotFoundException {
         List<Payment> paymentMethods = new ArrayList<>();
        String query = "SELECT PaymentMethodID, MethodName, Description, IsDeleted "
                + "FROM PaymentMethods "
                + "WHERE IsDeleted = 1";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Payment method = new Payment(
                        rs.getInt("PaymentMethodID"),
                        rs.getString("MethodName"),
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
                );
                paymentMethods.add(method);
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
    
    public void softDeletePaymentMethod(int PaymentMethodID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE PaymentMethods SET IsDeleted = 1 WHERE PaymentMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, PaymentMethodID);
            ps.executeUpdate();
        }
    }

    // 5) Xóa cứng (DELETE)
    public void hardDeletePaymentMethod(int PaymentMethodID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM PaymentMethods WHERE PaymentMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, PaymentMethodID);
            ps.executeUpdate();
        }
    }
    
    public void restorePaymentMethod(int PaymentMethodID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE PaymentMethods SET IsDeleted = 0 WHERE PaymentMethodID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, PaymentMethodID);
            ps.executeUpdate();
        }
    }
    
    public boolean deletePaymentMethod(int id) throws SQLException, ClassNotFoundException {
    String sql = "DELETE FROM PaymentMethods WHERE PaymentMethodID = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            System.out.println("No Payment method deleted - ID " + id + " not found or error!");
            return false;
        }
        System.out.println("Successfully deleted payment method with ID: " + id);
        return true;
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
                        rs.getString("Description"),
                        rs.getInt("IsDeleted")
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
