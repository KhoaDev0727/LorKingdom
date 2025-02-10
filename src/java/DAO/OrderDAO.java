/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Acer
 */
public class OrderDAO extends DBConnect.DBConnection {

    public List<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String query = "select * from [dbo].[Order]";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            String accountName = rs.getString("AccountName");
            String payMentMethodName = rs.getString("MethodName");
            String shipingMethodName = rs.getString("MethodName");
            while (rs.next()) {
                list.add(new Order(rs.getInt("OrderID"),
                        accountName,
                        payMentMethodName,
                        shipingMethodName,
                        rs.getDate("OrderDate"),
                        rs.getString("Status"),
                        rs.getFloat("TotalAmount"),
                        rs.getDate("CreatedAt"),
                        rs.getDate("UpdatedAt")));

            }
        }
        return list;
    }

    public void deleteOrder(int orderId) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM [dbo].[Order] WHERE OrderID = ?";

        // Kết nối cơ sở dữ liệu và thực hiện câu lệnh xóa
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Đặt giá trị cho tham số ?
            pstmt.setInt(1, orderId);

            // Thực thi câu lệnh DELETE
            int rowsAffected = pstmt.executeUpdate();

            // Kiểm tra số dòng bị ảnh hưởng và đưa ra thông báo phù hợp
            if (rowsAffected > 0) {
                System.out.println("Order deleted successfully.");
            } else {
                System.out.println("No order found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // phan trang
    public List<Order> getOrdersByPage(int page, int ordersPerPage) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        // Tạo câu lệnh SQL mà không có tham số trực tiếp
//        String query = "SELECT o.*, a.AccountName \n"
//                + "FROM [dbo].[Order] o JOIN [dbo].[Account] a ON o.AccountID = a.AccountID\n"
//                + "ORDER BY OrderID \n"
//                + "OFFSET " + ((page - 1) * ordersPerPage) + " ROWS "
//                + "FETCH NEXT " + ordersPerPage + " ROWS ONLY;";

        String query = "SELECT \n"
                + "    o.*, a.AccountName, p.MethodName, s.MethodName\n"
                + "FROM \n"
                + "    [dbo].[Order] o\n"
                + "INNER JOIN \n"
                + "    [dbo].[Account] a ON o.AccountID = a.AccountID\n"
                + "INNER JOIN \n"
                + "    [dbo].[PaymentMethod] p ON o.PaymentMethodID = p.PaymentMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[ShippingMethod] s ON o.ShippingMethodID = s.ShippingMethodID \n"
                + "ORDER BY OrderID \n"
                + "OFFSET " + ((page - 1) * ordersPerPage) + " ROWS "
                + "FETCH NEXT " + ordersPerPage + " ROWS ONLY;";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {  // Thực thi câu lệnh SQL trực tiếp
            // Lấy tên khách hàng (AccountName) từ bảng Account

            while (rs.next()) {
                String accountName = rs.getString("AccountName");
                String payMentMethodName = rs.getString("MethodName");
                String shipingMethodName = rs.getString("MethodName");
                list.add(new Order(rs.getInt("OrderID"),
                        accountName, // Thay vì AccountID, giờ là AccountName
                        payMentMethodName,
                        shipingMethodName,
                        rs.getDate("OrderDate"),
                        rs.getString("Status"),
                        rs.getFloat("TotalAmount"),
                        rs.getDate("CreatedAt"),
                        rs.getDate("UpdatedAt")));
            }
        }
        return list;
    }

    //phan trang
    public int getTotalOrders() throws SQLException, ClassNotFoundException {
        int total = 0;
        String query = "select count(*) from [dbo].[Order]";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<Order> searchByCustomerName(String customerName) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, a.AccountName, p.MethodName, s.MethodName\n"
                + "FROM \n"
                + "    [dbo].[Order] o\n"
                + "INNER JOIN \n"
                + "    [dbo].[Account] a ON o.AccountID = a.AccountID\n"
                + "INNER JOIN \n"
                + "    [dbo].[PaymentMethod] p ON o.PaymentMethodID = p.PaymentMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[ShippingMethod] s ON o.ShippingMethodID = s.ShippingMethodID\n"
                + "WHERE \n"
                + "    a.AccountName LIKE ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Đặt giá trị cho tham số tìm kiếm (customerName)
            pstmt.setString(1, "%" + customerName + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String accountName = rs.getString("AccountName");
                    String payMentMethodName = rs.getString("MethodName");
                    String shipingMethodName = rs.getString("MethodName");
                    list.add(new Order(rs.getInt("OrderID"),
                            accountName, // Thay vì AccountID, giờ là AccountName
                            payMentMethodName,
                            shipingMethodName,
                            rs.getDate("OrderDate"),
                            rs.getString("Status"),
                            rs.getFloat("TotalAmount"),
                            rs.getDate("CreatedAt"),
                            rs.getDate("UpdatedAt")));
                }
            }
        }
        return list;
    }
     public boolean isOrderExists(String Status) throws SQLException, ClassNotFoundException {
        String query = "Select COUNT(*) from [dbo].[Order] WHERE Status = ? ";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, Status);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
