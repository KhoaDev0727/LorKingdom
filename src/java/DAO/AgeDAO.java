/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Age;
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
public class AgeDAO {

    public List<Age> getAllAges() throws SQLException, ClassNotFoundException {
        List<Age> ages = new ArrayList<>();
        String query = "SELECT AgeID, AgeRange, CreatedAt, IsDeleted FROM Age";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ages.add(new Age(
                        rs.getInt("AgeID"),
                        rs.getString("AgeRange"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return ages;
    }

    public List<Age> getActiveAges() throws SQLException, ClassNotFoundException {
        List<Age> ages = new ArrayList<>();
        String query = "SELECT AgeID, AgeRange, CreatedAt, IsDeleted FROM Age WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ages.add(new Age(
                        rs.getInt("AgeID"),
                        rs.getString("AgeRange"),
                        rs.getDate("CreatedAt"),
                        rs.getInt("IsDeleted")
                ));
            }
        }
        return ages;
    }

    // Add new age
    public void addAge(Age age) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Age (AgeRange) VALUES (?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, age.getAgeRange());
            ps.executeUpdate();
        }
    }

    // Delete age by ID
    public void deleteAge(int ageID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Age SET IsDeleted = 1 WHERE AgeID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ageID);
            ps.executeUpdate();
        }
    }

    // Search age by keyword
    public List<Age> searchAge(String keyword) throws SQLException, ClassNotFoundException {
        List<Age> ages = new ArrayList<>();
        String query = "SELECT AgeID, AgeRange, CreatedAt, IsDeleted FROM Age WHERE AgeRange LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ages.add(new Age(
                            rs.getInt("AgeID"),
                            rs.getString("AgeRange"),
                            rs.getDate("CreatedAt"),
                            rs.getInt("IsDeleted")
                    ));
                }
            }
        }
        return ages;
    }

    // Check if age range already exists
    public boolean isAgeExists(String ageRange) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Age WHERE AgeRange = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ageRange);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void updateAge(Age age) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Age SET AgeRange = ? WHERE AgeID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, age.getAgeRange());
            ps.setInt(2, age.getAgeID());
            ps.executeUpdate();
        }
    }

    public String getAgeRangeByProductId(int productId) throws SQLException, ClassNotFoundException {
        String ageRange = null;
        String sql = "SELECT a.AgeRange FROM Product p JOIN Age a ON p.AgeID = a.AgeID WHERE p.ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ageRange = rs.getString("AgeRange");
                }
            }
        }
        return ageRange;
    }

    public void restoreAge(int ageID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Age SET IsDeleted = 0 WHERE AgeID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ageID);
            ps.executeUpdate();
        }
    }

}
