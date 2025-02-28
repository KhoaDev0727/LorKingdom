/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.AccountDAO.conn;
import static DAO.AccountDAO.stm;
import static DAO.ReviewDAO.conn;
import static DAO.ReviewDAO.rs;
import static DAO.ReviewDAO.stm;
import DBConnect.DBConnection;
import Model.Review;
import Model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class MyUntilsDAO {

    protected static String TOTAL_PAGE_STAFF = "SELECT COUNT(*) FROM Account Where RoleID = ? OR RoleID = ? OR RoleID = ? ";
    protected static String TOTAL_PAGE_CUSTOMER = "SELECT COUNT(*) FROM Account Where RoleID = ?";
    protected static String TOTAL_PAGE_REVIEW = "SELECT COUNT(*) FROM Reviews";
    protected static String TOTAL_PAGE_STAFF_SEARCH_STAFF = "SELECT COUNT(*) FROM Account WHERE ( AccountID = ? OR AccountName LIKE ? OR PhoneNumber LIKE ? OR email LIKE ? ) AND ( RoleID = ? OR RoleID = ? OR RoleID = ?)";
    protected static String TOTAL_PAGE_CUSTOMER_SEARCH_CUSTOMER = "SELECT COUNT(*) FROM Account WHERE ( AccountID = ? OR AccountName LIKE ? OR PhoneNumber LIKE ? OR email LIKE ? ) AND RoleID = ? ";
    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    public int getTotalPagesAccountStaff(int pageSize, int roleStaff, int roleAdmin, int roleWareHouse) throws ClassNotFoundException {
        int totalRecords = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TOTAL_PAGE_STAFF);
            stm.setInt(1, roleStaff);
            stm.setInt(2, roleWareHouse);
            stm.setInt(3, roleAdmin);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (int) Math.ceil((double) totalRecords / pageSize);
    }
    
    
    public int getTotalPagesAccountCustomer(int pageSize, int roleCustomer) throws ClassNotFoundException {
        int totalRecords = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TOTAL_PAGE_CUSTOMER);
            stm.setInt(1, roleCustomer);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    
    public int getTotalPagesAccountSearchsTAFF(int pageSize, int roleStaff, int roleAdmin, int roleWareHouse, String keyword) throws ClassNotFoundException {
        int totalRecords = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TOTAL_PAGE_STAFF_SEARCH_STAFF);
            // Check if keyword is a number (AccountID search)
            if (keyword.matches("\\d+")) {
                stm.setInt(1, Integer.parseInt(keyword)); // Search by AccountID
            } else {
                stm.setNull(1, java.sql.Types.INTEGER); // Ignore AccountID filter
            }
            // Wildcard search for other fields
            String searchPattern = "%" + keyword + "%";
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);
            stm.setString(4, searchPattern);
            stm.setInt(5, roleStaff);
            stm.setInt(6, roleWareHouse);
            stm.setInt(7, roleAdmin);

            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    totalRecords = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Consider proper logging instead
        }

        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public int getTotalPagesAccountSearchCustomer(int pageSize, int roleCustomer, String keyword) throws ClassNotFoundException {
        int totalRecords = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TOTAL_PAGE_CUSTOMER_SEARCH_CUSTOMER);
            // Check if keyword is a number (AccountID search)
            if (keyword.matches("\\d+")) {
                stm.setInt(1, Integer.parseInt(keyword)); // Search by AccountID
            } else {
                stm.setNull(1, java.sql.Types.INTEGER); // Ignore AccountID filter
            }
            // Wildcard search for other fields
            String searchPattern = "%" + keyword + "%";
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);
            stm.setString(4, searchPattern);
            stm.setInt(5, roleCustomer);

            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    totalRecords = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Consider proper logging instead
        }

        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public int getTotalPagesReview(int pageSize) throws ClassNotFoundException {
        int totalRecords = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TOTAL_PAGE_REVIEW);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    /* 
        TOTAL SEARCH REVIEW 
     */
    public int getTotalPagesSearchReview(int filterRating, int filterStatus, int filterUserIDOrProductID, int pageSize) throws ClassNotFoundException {
        int totalRecords = 0;

        String sql = "SELECT COUNT(*) FROM Reviews WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (filterUserIDOrProductID != 0) {
            sql += " AND (AccountID = ? OR ProductID = ?)";
            params.add(filterUserIDOrProductID);
            params.add(filterUserIDOrProductID);
        }
        if (filterRating != 0) {
            sql += " AND Rating = ?";
            params.add(filterRating);
        }
        if (filterStatus != -1) {
            sql += " AND Status = ?";
            params.add(filterStatus);
        }

        // Dùng try-with-resources để tự động đóng tài nguyên
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(sql);
            // Gán tham số
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    totalRecords = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) Math.ceil((double) totalRecords / pageSize);
    }

}
