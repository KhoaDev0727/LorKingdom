/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Sex;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class SexDAO {

    public List<Sex> getAllSexes() throws SQLException, ClassNotFoundException {
        List<Sex> sexes = new ArrayList<>();
        String query = "SELECT SexID, Name, IsDeleted, CreatedAt FROM Sex";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sexes.add(new Sex(
                        rs.getInt("SexID"),
                        rs.getString("Name"),
                        rs.getInt("IsDeleted"),
                        rs.getDate("CreatedAt")
                ));
            }
        }
        return sexes;
    }

    public List<Sex> getActiveSex() throws SQLException, ClassNotFoundException {
        List<Sex> sexes = new ArrayList<>();
        String query = "SELECT SexID, Name, IsDeleted, CreatedAt FROM Sex WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sexes.add(new Sex(
                        rs.getInt("SexID"),
                        rs.getString("Name"),
                        rs.getInt("IsDeleted"),
                        rs.getDate("CreatedAt")
                ));
            }
        }
        return sexes;
    }

    public void addSex(Sex sex) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Sex (Name) VALUES (?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, sex.getName());
            ps.executeUpdate();
        }
    }

    // Xóa giới tính theo ID
    // Kiểm tra giới tính đã tồn tại hay chưa
    public boolean isSexExists(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Sex WHERE Name = ?";
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

    public void updateSex(Sex sex) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Sex SET Name = ? WHERE SexID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, sex.getName());
            ps.setInt(2, sex.getSexID());
            ps.executeUpdate();
        }
    }

    public void deleteSex(int sexID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Sex WHERE SexID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            System.out.println("Deleting Sex ID: " + sexID); // Debug
            ps.setInt(1, sexID);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No rows deleted. Sex ID may not exist.");
            }
        }
    }

    public String getSexNameByProductId(int productId) throws SQLException, ClassNotFoundException {
        String sexName = null;
        String query = "SELECT s.Name "
                + "FROM Product p "
                + "JOIN Sex s ON p.SexID = s.SexID "
                + "WHERE p.ProductID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sexName = rs.getString("Name");
                }
            }
        }
        return sexName;
    }

    public List<Sex> searchSex(String keyword) throws SQLException, ClassNotFoundException {
        List<Sex> sexes = new ArrayList<>();
        String query = "SELECT SexID, Name FROM Sex WHERE LOWER(Name) LIKE LOWER(?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sexes.add(new Sex(
                            rs.getInt("SexID"),
                            rs.getString("Name"),
                            rs.getInt("IsDeleted"),
                            rs.getDate("CreatedAt")
                    ));
                }
            }
        }
        return sexes;
    }

}
