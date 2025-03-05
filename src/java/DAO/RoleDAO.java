/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class RoleDAO {
    // Lấy danh sách tất cả Role
public List<Role> getAllRoles() throws SQLException, ClassNotFoundException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT RoleID, RoleName, Description FROM Role";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                roles.add(new Role(
                        rs.getInt("RoleID"),
                        rs.getString("RoleName"), // lấy RoleName từ database và map vào thuộc tính Name của model
                        rs.getString("Description")
                ));
            }
        }
        return roles;
    }

    // Thêm Role mới
    public void addRole(Role role) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Role (RoleName, Description) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, role.getName()); // sử dụng getName() của model
            ps.setString(2, role.getDescription());
            ps.executeUpdate();
        }
    }

    // Kiểm tra sự tồn tại của Role theo tên
    public boolean isRole(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Role WHERE RoleName = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Cập nhật thông tin Role
    public void updateRole(Role role) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Role SET RoleName = ?, Description = ? WHERE RoleID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, role.getName());
            ps.setString(2, role.getDescription());
            ps.setInt(3, role.getRoleID());
            ps.executeUpdate();
        }
    }

    // Xóa Role theo ID
    public void deleteRole(int roleID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Role WHERE RoleID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            System.out.println("Deleting Role ID: " + roleID);
            ps.setInt(1, roleID);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No rows deleted. Role ID may not exist.");
            }
        }
    }

    // Tìm kiếm Role theo từ khóa (dựa trên tên)
    public List<Role> searchRoles(String keyword) throws SQLException, ClassNotFoundException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT RoleID, RoleName, Description FROM Role WHERE LOWER(RoleName) LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + keyword.trim().toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
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
}
