/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public List<Notification> getDeletedNotifications() throws SQLException, ClassNotFoundException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE IsDeleted = 1 ORDER BY CreatedAt DESC";

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

    public void updateNotification(int notificationID, String title, String content, String type, String status, Integer accountID) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Notifications SET Title = ?, Content = ?, Type = ?, Status = ?, AccountID = ? WHERE NotificationID = ? AND IsDeleted = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, type);
            stmt.setString(4, status);
            stmt.setObject(5, accountID, Types.INTEGER); // Nullable
            stmt.setInt(6, notificationID);
            stmt.executeUpdate();
        }
    }

    public void softDeleteNotification(int notificationID) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Notifications SET IsDeleted = 1 WHERE NotificationID = ? AND IsDeleted = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationID);
            stmt.executeUpdate();
        }
    }

    public void hardDeleteNotification(int notificationID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationID);
            stmt.executeUpdate();
        }
    }

    public void restoreNotification(int notificationID) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Notifications SET IsDeleted = 0 WHERE NotificationID = ? AND IsDeleted = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationID);
            stmt.executeUpdate();
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