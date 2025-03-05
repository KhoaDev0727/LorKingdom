package DAO;

import DBConnect.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Truong Van Khang - CE181852
 */
public class MyUntilsDAO {

    // SQL Queries
    private static final String TOTAL_PAGE_STAFF = "SELECT COUNT(*) FROM Account WHERE RoleID IN (?, ?, ?)";
    private static final String TOTAL_PAGE_CUSTOMER = "SELECT COUNT(*) FROM Account WHERE RoleID = ?";
    private static final String TOTAL_PAGE_REVIEW = "SELECT COUNT(*) FROM Reviews WHERE IsDeleted = ?";
    private static final String TOTAL_PAGE_STAFF_SEARCH = 
        "SELECT COUNT(*) FROM Account WHERE (AccountID = ? OR AccountName LIKE ? OR PhoneNumber LIKE ? OR email LIKE ?) AND RoleID IN (?, ?, ?)";
    private static final String TOTAL_PAGE_CUSTOMER_SEARCH = 
        "SELECT COUNT(*) FROM Account WHERE (AccountID = ? OR AccountName LIKE ? OR PhoneNumber LIKE ? OR email LIKE ?) AND RoleID = ?";

    /**
     * Helper method to calculate total pages based on total records and page size
     */
    private int calculateTotalPages(int totalRecords, int pageSize) {
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // Các hàm không liên quan đến review giữ nguyên
    public int getTotalPagesAccountStaff(int pageSize, int roleStaff, int roleAdmin, int roleWareHouse) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stm = conn.prepareStatement(TOTAL_PAGE_STAFF)) {
            stm.setInt(1, roleStaff);
            stm.setInt(2, roleAdmin);
            stm.setInt(3, roleWareHouse);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return calculateTotalPages(rs.getInt(1), pageSize);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching total pages for staff accounts: " + e.getMessage(), e);
        }
        return 0;
    }

    public int getTotalPagesAccountCustomer(int pageSize, int roleCustomer) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stm = conn.prepareStatement(TOTAL_PAGE_CUSTOMER)) {
            stm.setInt(1, roleCustomer);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return calculateTotalPages(rs.getInt(1), pageSize);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching total pages for customer accounts: " + e.getMessage(), e);
        }
        return 0;
    }

    public int getTotalPagesAccountSearchStaff(int pageSize, int roleStaff, int roleAdmin, int roleWareHouse, String keyword) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stm = conn.prepareStatement(TOTAL_PAGE_STAFF_SEARCH)) {
            setSearchParameters(stm, keyword, roleStaff, roleAdmin, roleWareHouse);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return calculateTotalPages(rs.getInt(1), pageSize);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching total pages for staff search: " + e.getMessage(), e);
        }
        return 0;
    }

    public int getTotalPagesAccountSearchCustomer(int pageSize, int roleCustomer, String keyword) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stm = conn.prepareStatement(TOTAL_PAGE_CUSTOMER_SEARCH)) {
            setSearchParameters(stm, keyword, roleCustomer);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return calculateTotalPages(rs.getInt(1), pageSize);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching total pages for customer search: " + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Get total pages for reviews based on IsDeleted status
     */
    public int getTotalPagesReview(int pageSize, int isDeleted) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stm = conn.prepareStatement(TOTAL_PAGE_REVIEW)) {
            stm.setInt(1, isDeleted);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return calculateTotalPages(rs.getInt(1), pageSize);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching total pages for reviews: " + e.getMessage(), e);
        }
        return 0; // Trả về 0 nếu không có bản ghi
    }

    /**
     * Get total pages for reviews with search filters
     */
    public int getTotalPagesSearchReview(int filterRating, int filterStatus, int filterUserIDOrProductID, int pageSize) throws SQLException, ClassNotFoundException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Reviews WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filterUserIDOrProductID != 0) {
            sql.append(" AND (AccountID = ? OR ProductID = ?)");
            params.add(filterUserIDOrProductID);
            params.add(filterUserIDOrProductID);
        }
        if (filterRating != 0) {
            sql.append(" AND Rating = ?");
            params.add(filterRating);
        }
        if (filterStatus != -1) {
            sql.append(" AND Status = ?");
            params.add(filterStatus);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return calculateTotalPages(rs.getInt(1), pageSize);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching total pages for review search: " + e.getMessage(), e);
        }
        return 0; // Trả về 0 nếu không có bản ghi
    }

    // Helper methods giữ nguyên
    private void setSearchParameters(PreparedStatement stm, String keyword, int roleStaff, int roleAdmin, int roleWareHouse) throws SQLException {
        if (keyword != null && keyword.matches("\\d+")) {
            stm.setInt(1, Integer.parseInt(keyword));
        } else {
            stm.setNull(1, java.sql.Types.INTEGER);
        }
        String searchPattern = "%" + (keyword != null ? keyword : "") + "%";
        stm.setString(2, searchPattern);
        stm.setString(3, searchPattern);
        stm.setString(4, searchPattern);
        stm.setInt(5, roleStaff);
        stm.setInt(6, roleAdmin);
        stm.setInt(7, roleWareHouse);
    }

    private void setSearchParameters(PreparedStatement stm, String keyword, int roleCustomer) throws SQLException {
        if (keyword != null && keyword.matches("\\d+")) {
            stm.setInt(1, Integer.parseInt(keyword));
        } else {
            stm.setNull(1, java.sql.Types.INTEGER);
        }
        String searchPattern = "%" + (keyword != null ? keyword : "") + "%";
        stm.setString(2, searchPattern);
        stm.setString(3, searchPattern);
        stm.setString(4, searchPattern);
        stm.setInt(5, roleCustomer);
    }
}