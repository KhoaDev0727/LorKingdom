package DAO;

import DBConnect.DBConnection;
import Model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Notification> getAllNotifications() throws SQLException, ClassNotFoundException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE IsDeleted = 0 ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("NotificationID"),
                        rs.getString("Title"),
                        rs.getString("Content"),
                        rs.getString("Type"),
                        rs.getString("Status"),
                        rs.getObject("AccountID") != null ? rs.getInt("AccountID") : null,
                        rs.getBoolean("IsDeleted"),
                        rs.getTimestamp("CreatedAt")
                ));
            }
        }
        return notifications;
    }

    public void addNotification(String title, String content, String type, Integer accountID) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Notifications (Title, Content, Type, Status, AccountID, CreatedAt) VALUES (?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, type);
            stmt.setString(4, "Unread"); // Default per schema
            stmt.setObject(5, accountID, Types.INTEGER); // Nullable
            stmt.executeUpdate();
        }
    }

    public void updateNotification(int id, String title, String content, String type, String status, Integer accountID) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Notifications SET Title = ?, Content = ?, Type = ?, Status = ?, AccountID = ? WHERE NotificationID = ? AND IsDeleted = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, type);
            stmt.setString(4, status);
            stmt.setObject(5, accountID, Types.INTEGER); // Nullable
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public boolean deleteNotification(int id) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Notifications SET IsDeleted = 1 WHERE NotificationID = ? AND IsDeleted = 0"; // Soft delete
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Notification> searchNotifications(String keyword) throws SQLException, ClassNotFoundException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE (LOWER(Title) LIKE LOWER(?) OR LOWER(Content) LIKE LOWER(?)) AND IsDeleted = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + (keyword != null ? keyword.toLowerCase() : "") + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new Notification(
                            rs.getInt("NotificationID"),
                            rs.getString("Title"),
                            rs.getString("Content"),
                            rs.getString("Type"),
                            rs.getString("Status"),
                            rs.getObject("AccountID") != null ? rs.getInt("AccountID") : null,
                            rs.getBoolean("IsDeleted"),
                            rs.getTimestamp("CreatedAt")
                    ));
                }
            }
        }
        return notifications;
    }
}