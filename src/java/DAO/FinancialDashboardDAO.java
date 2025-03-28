/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.CategorySales;
import Model.RevenueData;
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
public class FinancialDashboardDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    private static String SELECT_TOTALSOLD = "SELECT SUM(Quantity) AS TotalSold FROM OrderDetails;";
    private static String SELECET_TOTALREVENUE = "SELECT SUM(Total) AS TotalRevenue FROM OrderDetails ;";
    private static String SELECT_TOTAL_CUSTOMER = "SELECT COUNT(*) AS TotalCustomers FROM Account WHERE RoleID = 3;";
    private static final String SELECT_REVENUE_DATA = "SELECT FORMAT(OrderDate, 'MM/yyyy') AS MonthYear, "
            + "SUM(TotalAmount) AS Revenue FROM Orders WHERE IsDeleted = 0 "
            + "GROUP BY FORMAT(OrderDate, 'MM/yyyy') ORDER BY MIN(OrderDate)";
    private static final String SELECT_CategorySales_DATA = "SELECT c.Name AS Category, "
            + "SUM(od.Quantity) AS Sales "
            + "FROM OrderDetails od "
            + "JOIN Product p ON od.ProductID = p.ProductID "
            + "JOIN Category c ON p.CategoryID = c.CategoryID "
            + "WHERE p.IsDeleted = 0 AND od.IsDeleted = 0 "
            + "GROUP BY c.Name;";
    private static final String SELECT_LAST_NMONTHS_REVENUE
            = "SELECT TOP (?) "
            + "    FORMAT(OrderDate, 'MM/yyyy') AS MonthYear, "
            + "    SUM(TotalAmount) AS Revenue "
            + "FROM Orders "
            + "WHERE IsDeleted = 0 "
            + "    AND OrderDate >= DATEADD(MONTH, -(? - 1), DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)) "
            + "    AND OrderDate < DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) "
            + "GROUP BY FORMAT(OrderDate, 'MM/yyyy') "
            + "ORDER BY MAX(OrderDate) DESC";

    public static List<RevenueData> getRevenueData() throws SQLException, ClassNotFoundException {
        List<RevenueData> data = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stm = conn.prepareStatement(SELECT_REVENUE_DATA);  ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                RevenueData item = new RevenueData();
                item.setMonthYear(rs.getString("MonthYear"));  // Fix setter name
                item.setRevenue(rs.getDouble("Revenue"));
                data.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Consider logging instead of printing
        }
        return data;
    }

    public static List<CategorySales> getCategorySales() throws SQLException, ClassNotFoundException {
        List<CategorySales> data = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stm = conn.prepareStatement(SELECT_CategorySales_DATA);  ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                CategorySales item = new CategorySales();
                item.setCategoryName(rs.getString("Category"));
                item.setQuantityCartegory(rs.getInt("Sales"));
                data.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Nên thay bằng logger
        }
        return data;
    }

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

    public static List<RevenueData> getLastMonthsRevenue(int months) throws SQLException, ClassNotFoundException {
        if (months <= 0) {
            throw new IllegalArgumentException("Số tháng phải lớn hơn 0");
        }

        List<RevenueData> data = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stm = conn.prepareStatement(SELECT_LAST_NMONTHS_REVENUE)) {

            stm.setInt(1, months); // Tham số cho TOP
            stm.setInt(2, months); // Tham số cho DATEADD

            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    RevenueData item = new RevenueData();
                    item.setMonthYear(rs.getString("MonthYear"));
                    item.setRevenue(rs.getDouble("Revenue"));
                    data.add(item);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return data;
    }

}
