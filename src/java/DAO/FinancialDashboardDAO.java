/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.ReviewDAO.conn;
import DBConnect.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class FinancialDashboardDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    private static String SELECET_TOTALREVENUE = "SELECT SUM(Total) AS TotalRevenue FROM OrderDetail;";
    private static String SELECT_TOTALSOLD = "SELECT SUM(Quantity) AS TotalSold FROM OrderDetail;";
    private static String SELECT_TOTAL_CUSTOMER = "SELECT COUNT(*) AS TotalCustomers FROM Account;";

    public static long showTotalSold() {
        long totalSold = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_TOTALSOLD);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalSold = rs.getLong("TotalSold");
            }
            return totalSold;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalSold;
    }

    public static long showTotalRevenue() {
        long totalRevenue = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECET_TOTALREVENUE);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getLong("TotalRevenue");
            }
            return totalRevenue;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    public static int showTotalCustomer() {
        int totalCustomer = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_TOTAL_CUSTOMER);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalCustomer = rs.getInt("TotalCustomers");
            }
            return totalCustomer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCustomer;
    }

}
