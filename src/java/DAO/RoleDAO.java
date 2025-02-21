package DAO;

import DBConnect.DBConnection;
import Model.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public List<Role> getAllRoles() throws SQLException, ClassNotFoundException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new Role(
                        rs.getInt("RoleID"),
                        rs.getString("RoleName"),
                        rs.getString("Description")
                ));
            }
        }
        return roles;
    }

    public void addRole(String roleName, String description) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Role (RoleName, Description) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }
    }

    public void updateRole(int id, String roleName, String description) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Role SET RoleName = ?, Description = ? WHERE RoleID = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            stmt.setString(2, description);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public void deleteRole(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Role WHERE RoleID = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Role> searchRoles(String keyword) throws SQLException, ClassNotFoundException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role WHERE LOWER(RoleName) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(new Role(
                            rs.getInt("RoleID"),
                            rs.getString("RoleName"),
                            rs.getString("Description")
                    ));
                }
            }
        }
        return roles;
    }

    public boolean isRoleExists(String roleName) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Role WHERE RoleName = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}