/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Order;
import Model.OrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class OrderDAO extends DBConnect.DBConnection {

    public List<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String query = "SELECT \n"
                + "                    o.*, a.AccountName, p.MethodName, s.MethodName, k.Status\n"
                + "FROM \n"
                + "    [dbo].[Orders] o\n"
                + "INNER JOIN \n"
                + "    [dbo].[Account] a ON o.AccountID = a.AccountID\n"
                + "INNER JOIN \n"
                + "    [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[StatusOrder] k ON o.StatusID = k.StatusID\n";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String accountName = rs.getString("AccountName");
                String payMentMethodName = rs.getString("MethodName");
                String shipingMethodName = rs.getString("MethodName");
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
        String query = "DELETE FROM [dbo].[Orders] WHERE OrderID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderId);

            int rowsAffected = pstmt.executeUpdate();

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
        String query = "SELECT \n"
                + "                    o.*, a.AccountName, p.MethodName, s.MethodName, k.Status\n"
                + "FROM \n"
                + "    [dbo].[Orders] o\n"
                + "INNER JOIN \n"
                + "    [dbo].[Account] a ON o.AccountID = a.AccountID\n"
                + "INNER JOIN \n"
                + "    [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[StatusOrder] k ON o.StatusID = k.StatusID\n"
                + "ORDER BY OrderID \n"
                + "OFFSET " + ((page - 1) * ordersPerPage) + " ROWS "
                + "FETCH NEXT " + ordersPerPage + " ROWS ONLY;";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {  // Thực thi câu lệnh SQL trực tiếp
            // Lấy tên khách hàng (AccountName) từ bảng Account

            while (rs.next()) {
                String accountName = rs.getString("AccountName");
                String payMentMethodName = rs.getString("MethodName");
                String shipingMethodName = rs.getString("MethodName");
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

    //phan trang
    public int getTotalOrders() throws SQLException, ClassNotFoundException {
        int total = 0;
        String query = "select count(*) from [dbo].[Orders]";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
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
                + "                    o.*, a.AccountName, p.MethodName, s.MethodName, k.Status\n"
                + "FROM \n"
                + "    [dbo].[Orders] o\n"
                + "INNER JOIN \n"
                + "    [dbo].[Account] a ON o.AccountID = a.AccountID\n"
                + "INNER JOIN \n"
                + "    [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[StatusOrder] k ON o.StatusID = k.StatusID\n"
                + "WHERE \n"
                + "    a.AccountName LIKE ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Đặt giá trị cho tham số tìm kiếm (customerName)
            pstmt.setString(1, "%" + customerName + "%");

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String accountName = rs.getString("AccountName");
                    String shipingMethodName = rs.getString("MethodName");
                    String payMentMethodName = rs.getString("MethodName");

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
        String query = "Select COUNT(*) from [dbo].[Orders] WHERE Status = ? ";
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

    public List<Order> searchByMoney(Double minAmount) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String query = "SELECT \n"
                + "                    o.*, a.AccountName, p.MethodName, s.MethodName, k.Status\n"
                + "FROM \n"
                + "    [dbo].[Orders] o\n"
                + "INNER JOIN \n"
                + "    [dbo].[Account] a ON o.AccountID = a.AccountID\n"
                + "INNER JOIN \n"
                + "    [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID\n"
                + "INNER JOIN \n"
                + "    [dbo].[StatusOrder] k ON o.StatusID = k.StatusID\n"
                + "WHERE \n"
                + "    o.TotalAmount <= ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Đặt giá trị cho tham số tìm kiếm (minAmount)
            pstmt.setDouble(1, minAmount);

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String accountName = rs.getString("AccountName");
                    String shippingMethodName = rs.getString("MethodName");
                    String paymentMethodName = rs.getString("MethodName");

                    list.add(new Order(rs.getInt("OrderID"),
                            accountName, // Thay vì AccountID, giờ là AccountName
                            paymentMethodName,
                            shippingMethodName,
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

    // Phương thức tìm kiếm theo số tiền với sắp xếp theo ASC hoặc DESC
    public List<Order> sort(String sortOrder) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();

        // Kiểm tra và thiết lập thứ tự sắp xếp (ASC hoặc DESC)
        String orderBy = "ASC"; // Mặc định là ASC
        if ("DESC".equalsIgnoreCase(sortOrder)) {
            orderBy = "DESC"; // Sắp xếp theo thứ tự giảm dần nếu tham số là DESC
        }

        // Câu truy vấn SQL với sắp xếp theo TotalAmount
        String query = "SELECT o.*, a.AccountName, p.MethodName AS PaymentMethodName, s.MethodName AS ShippingMethodName, g.Status "
                + "FROM [dbo].[Orders] o "
                + "INNER JOIN [dbo].[Account] a ON o.AccountID = a.AccountID "
                + "INNER JOIN [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID "
                + "INNER JOIN [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID "
                + "INNER JOIN [dbo].[StatusOrder] g ON o.StatusID = g.StatusID "
                + "WHERE o.TotalAmount IS NOT NULL "
                + "ORDER BY o.TotalAmount " + orderBy; // Sắp xếp theo TotalAmount với orderBy

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                            rs.getInt("OrderID"),
                            rs.getString("AccountName"),
                            rs.getString("PaymentMethodName"),
                            rs.getString("ShippingMethodName"),
                            rs.getDate("OrderDate"),
                            rs.getString("Status"),
                            rs.getFloat("TotalAmount"),
                            rs.getDate("CreatedAt"),
                            rs.getDate("UpdatedAt")
                    ));
                }
            }
        }
        return list;
    }

//    Cua Khang 
    public static List<Order> getOrdersByUser(int accountId, int status) {
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>();
        String sql = "SELECT o.OrderID, o.OrderDate, o.TotalAmount, s.Status, \n"
                + "       d.ProductID, p.Name, d.Quantity, d.UnitPrice,  \n"
                + "       (SELECT TOP 1 Image FROM ProductImages \n"
                + "        WHERE ProductID = d.ProductID AND IsMain = 1) AS ProductImage,\n"
                + "       c.Name AS CategoryName  \n"
                + "FROM Orders o \n"
                + "JOIN StatusOrder s ON o.StatusID = s.StatusID \n"
                + "JOIN OrderDetails d ON o.OrderID = d.OrderID \n"
                + "JOIN Product p ON d.ProductID = p.ProductID \n"
                + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID \n"
                + "WHERE o.AccountID = ? AND s.StatusID = ? \n"
                + "ORDER BY o.OrderDate DESC;";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);   // Set the first placeholder to accountId
            ps.setInt(2, status);   // Set the second placeholder to status
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                Order order = orderMap.get(orderId);

                if (order == null) {
                    order = new Order();
                    order.setOrderId(orderId);
                    order.setOrderDate(rs.getDate("OrderDate"));
                    order.setTotalAmount(rs.getFloat("TotalAmount"));
                    order.setStatus(rs.getString("Status"));
                    orderMap.put(orderId, order);
                    orders.add(order);
                }

                OrderDetail detail = new OrderDetail();
                detail.setProductID(rs.getInt("ProductID"));
                detail.setProductName(rs.getString("Name"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getDouble("UnitPrice"));
                detail.setProductImage(rs.getString("ProductImage"));
                detail.setCategoryName(rs.getString("CategoryName"));
                order.addOrderDetail(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return orders;
    }
}
