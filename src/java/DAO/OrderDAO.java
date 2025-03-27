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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer
 */
public class OrderDAO extends DBConnect.DBConnection {

    public List<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    a.AccountName, \n"
                + "    p.MethodName AS PaymentMethodName, \n"
                + "    s.MethodName AS ShippingMethodName, \n"
                + "    k.Status\n"
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
                list.add(new Order(rs.getInt("OrderID"),
                        rs.getString("AccountName"),
                        rs.getString("PaymentMethodName"),
                        rs.getString("ShippingMethodName"),
                        rs.getDate("OrderDate"),
                        rs.getString("Status"),
                        rs.getFloat("TotalAmount"),
                        rs.getDate("CreatedAt"),
                        rs.getDate("UpdatedAt")));
            }
        }
        return list;
    }

    public Order getOrderByDelete(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT \n"
                + "    o.*, \n"
                + "    a.AccountName, \n"
                + "    p.MethodName AS PaymentMethodName, \n"
                + "    s.MethodName AS ShippingMethodName, \n"
                + "    k.Status\n"
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
                + "    o.OrderID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                order.setAccountName(rs.getString("AccountName"));
                order.setPayMentMethodName(rs.getString("PaymentMethodName"));
                order.setShipingMethodName(rs.getString("ShippingMethodName"));
                order.setOrderDate(rs.getDate("OrderDate"));
                order.setStatus(rs.getString("Status"));
                order.setTotalAmount(rs.getFloat("TotalAmount"));
                order.setCreatedAt(rs.getDate("CreatedAt"));
                order.setUpdatedAt(rs.getDate("UpdatedAt"));
                return order;
            }
        }
        return null;
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

    public List<Order> getOrdersByPage(int page, int ordersPerPage) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    a.AccountName, \n"
                + "    p.MethodName AS PaymentMethodName, \n"
                + "    s.MethodName AS ShippingMethodName, \n"
                + "    k.Status\n"
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

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Order(rs.getInt("OrderID"),
                        rs.getString("AccountName"),
                        rs.getString("PaymentMethodName"),
                        rs.getString("ShippingMethodName"),
                        rs.getDate("OrderDate"),
                        rs.getString("Status"),
                        rs.getFloat("TotalAmount"),
                        rs.getDate("CreatedAt"),
                        rs.getDate("UpdatedAt")));
            }
        }
        return list;
    }

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
                + "    o.*, \n"
                + "    a.AccountName, \n"
                + "    p.MethodName AS PaymentMethodName, \n"
                + "    s.MethodName AS ShippingMethodName, \n"
                + "    k.Status\n"
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

            pstmt.setString(1, "%" + customerName + "%");

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(rs.getInt("OrderID"),
                            rs.getString("AccountName"),
                            rs.getString("PaymentMethodName"),
                            rs.getString("ShippingMethodName"),
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
                + "    o.*, \n"
                + "    a.AccountName, \n"
                + "    p.MethodName AS PaymentMethodName, \n"
                + "    s.MethodName AS ShippingMethodName, \n"
                + "    k.Status\n"
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

            pstmt.setDouble(1, minAmount);

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(rs.getInt("OrderID"),
                            rs.getString("AccountName"),
                            rs.getString("PaymentMethodName"),
                            rs.getString("ShippingMethodName"),
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

    public List<Order> sort(String sortOrder) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String orderBy = "ASC";
        if ("DESC".equalsIgnoreCase(sortOrder)) {
            orderBy = "DESC";
        }

        String query = "SELECT o.*, a.AccountName, "
                + "p.MethodName AS PaymentMethodName, "
                + "s.MethodName AS ShippingMethodName, "
                + "k.Status "
                + "FROM [dbo].[Orders] o "
                + "INNER JOIN [dbo].[Account] a ON o.AccountID = a.AccountID "
                + "INNER JOIN [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID "
                + "INNER JOIN [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID "
                + "INNER JOIN [dbo].[StatusOrder] k ON o.StatusID = k.StatusID "
                + "WHERE o.TotalAmount IS NOT NULL "
                + "ORDER BY o.TotalAmount " + orderBy;

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
    public Order getOrderByIdUpdate(int orderId) throws SQLException, ClassNotFoundException {
    String sql = "SELECT o.*, a.AccountName, "
            + "p.MethodName AS PaymentMethodName, "
            + "s.MethodName AS ShippingMethodName, "
            + "st.Status "
            + "FROM [dbo].[Orders] o "
            + "INNER JOIN [dbo].[Account] a ON o.AccountID = a.AccountID "
            + "INNER JOIN [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID "
            + "INNER JOIN [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID "
            + "INNER JOIN [dbo].[StatusOrder] st ON o.StatusID = st.StatusID "
            + "WHERE o.OrderID = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            Order order = new Order();
            order.setOrderId(rs.getInt("OrderID"));
            order.setAccountName(rs.getString("AccountName"));
            order.setPayMentMethodName(rs.getString("PaymentMethodName"));
            order.setShipingMethodName(rs.getString("ShippingMethodName"));
            order.setOrderDate(rs.getTimestamp("OrderDate"));
            order.setStatus(rs.getString("Status")); // This now comes from StatusOrder table
            order.setTotalAmount(rs.getDouble("TotalAmount"));
            order.setCreatedAt(rs.getTimestamp("CreatedAt"));
            order.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
            return order;
        }
    }
    return null;
}

//    Creator by Khang
    public static Map<String, Object> getOrdersByUser(int accountId, int status, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>();

        // Sửa lỗi cú pháp SQL
        String sql = "WITH OrderCTE AS (\n"
                + "    SELECT o.OrderID, o.OrderDate, o.TotalAmount, s.Status,\n" // Thêm dấu phẩy sau s.Status
                + "           ROW_NUMBER() OVER (ORDER BY o.OrderDate DESC) AS RowNum\n"
                + "    FROM Orders o\n"
                + "    JOIN StatusOrder s ON o.StatusID = s.StatusID\n"
                + "    WHERE o.AccountID = ? AND s.StatusID = ?\n"
                + "),\n"
                + "PagedOrders AS (\n"
                + "    SELECT OrderID, OrderDate, TotalAmount, Status \n"
                + "    FROM OrderCTE\n"
                + "    WHERE RowNum BETWEEN ? AND ?\n"
                + ")\n"
                + "SELECT po.OrderID, po.OrderDate, po.TotalAmount, po.Status,\n" // Thêm dấu phẩy sau po.Status
                + "       d.ProductID, p.Name AS ProductName, d.Quantity, d.UnitPrice,d.Total ,d.Discount, d.Reviewed,d.OrderDetailID, d.OrderID,\n"
                + "       (SELECT TOP 1 Image FROM ProductImages \n"
                + "        WHERE ProductID = d.ProductID AND IsMain = 1) AS ProductImage,\n"
                + "       c.Name AS CategoryName\n"
                + "FROM PagedOrders po\n"
                + "JOIN OrderDetails d ON po.OrderID = d.OrderID\n"
                + "JOIN Product p ON d.ProductID = p.ProductID\n"
                + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID\n"
                + "ORDER BY po.OrderDate DESC;";

        String countSql = "SELECT COUNT(DISTINCT o.OrderID) AS TotalOrders "
                + "FROM Orders o "
                + "JOIN StatusOrder s ON o.StatusID = s.StatusID "
                + "WHERE o.AccountID = ? AND s.StatusID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  PreparedStatement countPs = conn.prepareStatement(countSql)) {

            // Tính toán phân trang
            int offset = (page - 1) * pageSize;
            int startRow = offset + 1;
            int endRow = offset + pageSize;

            // Đếm tổng số đơn
            countPs.setInt(1, accountId);
            countPs.setInt(2, status);
            ResultSet countRs = countPs.executeQuery();
            int total = countRs.next() ? countRs.getInt("TotalOrders") : 0;
            result.put("total", total);

            // Chỉ thực hiện truy vấn chính nếu có đơn hàng
            if (total > 0) {
                // Lấy dữ liệu đơn hàng
                ps.setInt(1, accountId);
                ps.setInt(2, status);
                ps.setInt(3, startRow);
                ps.setInt(4, endRow);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("OrderID");
                    Order order = orderMap.get(orderId);

                    if (order == null) {
                        order = new Order();
                        order.setOrderId(orderId);
                        order.setOrderDate(rs.getTimestamp("OrderDate")); // Sử dụng Timestamp để giữ thời gian
                        order.setTotalAmount(rs.getDouble("TotalAmount"));
                        order.setStatus(rs.getString("Status"));
                        orderMap.put(orderId, order);
                        orders.add(order);
                    }

                    OrderDetail detail = new OrderDetail();

                    detail.setOrderDetailID(rs.getInt("orderDetailID"));
                    detail.setOrderID(rs.getInt("OrderID"));
                    detail.setProductID(rs.getInt("ProductID"));
                    detail.setProductName(rs.getString("ProductName"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setUnitPrice(rs.getDouble("UnitPrice"));
                    detail.setDiscount(rs.getFloat("Discount"));
                    detail.setProductImage(rs.getString("ProductImage"));
                    detail.setCategoryName(rs.getString("CategoryName"));
                    detail.setTotalPrice(rs.getDouble("Total"));
                    detail.setReviewed(rs.getInt("Reviewed")); // Giả sử Reviewed là BIT trong SQL
                    order.addOrderDetail(detail);
                }
            }

            result.put("orders", orders);
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý exception phù hợp ở đây
            result.put("error", "An error occurred while fetching orders");
        }
        return result;
    }

    public static HashMap<Integer, Integer> getAllProductInOrder(int orderId) throws SQLException, ClassNotFoundException {   // HashMap để lưu ProductID và Quantity
        HashMap<Integer, Integer> productQuantityMap = new HashMap<>();

        String sql = "SELECT ProductID, Quantity "
                + "FROM OrderDetails "
                + "WHERE OrderID = ?;";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stm = conn.prepareStatement(sql)) {

            // Thiết lập OrderID cần truy vấn
            stm.setInt(1, orderId); // Thay số 1 bằng OrderID mong muốn
            ResultSet rs = stm.executeQuery();

            // Duyệt kết quả và lưu vào HashMap
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                int quantity = rs.getInt("Quantity");
                productQuantityMap.put(productId, quantity);
            }

            // Hiển thị kết quả
            System.out.println("Danh sách sản phẩm và số lượng:");
            for (Integer productId : productQuantityMap.keySet()) {
                System.out.println("ProductID: " + productId + ", Quantity: " + productQuantityMap.get(productId));
            }
            return productQuantityMap;
        }
    }

    public HashMap<String, Object> getOrderDetails(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT \n"
                + "    o.OrderID, \n"
                + "    s.Status, \n"
                + "    o.OrderDate, \n"
                + "    pm.MethodName AS PaymentMethodName, \n"
                + "    sm.MethodName AS ShippingMethodName, \n"
                + "    p.Name AS ProductName, \n"
                + "    d.Quantity, \n"
                + "    d.UnitPrice, \n"
                + "    d.Discount, \n"
                + "    d.Total, \n"
                + "    d.Reviewed, \n"
                + "    (SELECT TOP 1 Image FROM ProductImages WHERE ProductID = d.ProductID AND IsMain = 1) AS ProductImage, \n"
                + "    c.Name AS CategoryName \n"
                + "FROM Orders o \n"
                + "JOIN OrderDetails d ON o.OrderID = d.OrderID \n"
                + "JOIN PaymentMethods pm ON o.PaymentMethodID = pm.PaymentMethodID \n"
                + "JOIN ShippingMethods sm ON o.ShippingMethodID = sm.ShippingMethodID \n"
                + "JOIN StatusOrder s ON o.StatusID = s.StatusID \n"
                + "JOIN Product p ON d.ProductID = p.ProductID \n"
                + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID \n"
                + "WHERE o.OrderID = ?;";

        HashMap<String, Object> result = new HashMap<>();
        Order order = null;

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getInt("OrderID"));

                String status = rs.getString("Status");
                String statusOrder;
                if (status.equals("Pending")) {
                    statusOrder = "Đang Xử Lí";
                } else if (status.equals("Shipped")) {
                    statusOrder = "Đang Vận Chuyển";
                } else if (status.equals("Delivered")) {
                    statusOrder = "Đã Nhận Hàng";
                } else {
                    statusOrder = "Đã Hủy";
                }
                order.setStatus(statusOrder);
                order.setOrderDate(rs.getTimestamp("OrderDate"));
                order.setPayMentMethodName(rs.getString("PaymentMethodName"));
                order.setShipingMethodName(rs.getString("ShippingMethodName"));

                List<OrderDetail> orderDetails = new ArrayList<>();
                do {
                    OrderDetail detail = new OrderDetail();
                    detail.setProductName(rs.getString("ProductName"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setUnitPrice(rs.getDouble("UnitPrice"));
                    detail.setDiscount(rs.getFloat("Discount"));
                    detail.setTotalPrice(rs.getDouble("Total"));
                    detail.setReviewed(rs.getInt("Reviewed"));
                    detail.setProductImage(rs.getString("ProductImage"));
                    detail.setCategoryName(rs.getString("CategoryName"));
                    orderDetails.add(detail);
                } while (rs.next());

                result.put("order", order);
                result.put("orderDetails", orderDetails);
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy thông tin chi tiết đơn hàng: " + e.getMessage());
        }
        return result;
    }

    //Create by Khoa
    public boolean updateOrderStatus(int orderId, int statusId) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Orders SET StatusID = ?, UpdatedAt = GETDATE() WHERE OrderID = ?";
        boolean rowUpdate = false;
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, statusId);
            ps.setInt(2, orderId);
            rowUpdate = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
        }
        return rowUpdate;
    }

    public int getStatusIdByName(String statusName) throws SQLException, ClassNotFoundException {
        int statusId = -1;
        String query = "SELECT StatusID FROM StatusOrder WHERE Status = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, statusName);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    statusId = rs.getInt("StatusID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi lấy StatusID: " + e.getMessage());
        }
        return statusId;
    }

    public boolean saveOrder(Order order, int userId, String address, String phone, String email, String city, String district, String ward) {
        String insertOrderSQL = "INSERT INTO Orders (AccountID, StatusID, PaymentMethodID, ShippingMethodID, OrderDate, TotalAmount, CreatedAt) "
                + "VALUES (?, (SELECT StatusID FROM StatusOrder WHERE Status = ?), "
                + "(SELECT PaymentMethodID FROM PaymentMethods WHERE MethodName = ?), "
                + "(SELECT ShippingMethodID FROM ShippingMethods WHERE MethodName = ?), ?, ?, ?)";

        String insertOrderDetailSQL = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice, Discount) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();

            if (conn == null) {
                Logger.getLogger(OrderDAO.class
                        .getName()).log(Level.SEVERE, "Không thể kết nối đến cơ sở dữ liệu!");
                return false;
            }
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Lưu vào bảng Orders
            PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, userId);
            psOrder.setString(2, order.getStatus());
            psOrder.setString(3, order.getPayMentMethodName());
            psOrder.setString(4, order.getShipingMethodName());
            psOrder.setTimestamp(5, new java.sql.Timestamp(order.getOrderDate().getTime()));
            psOrder.setDouble(6, order.getTotalAmount());
            psOrder.setTimestamp(7, new java.sql.Timestamp(order.getCreatedAt().getTime()));
            psOrder.executeUpdate();

            // Lấy OrderID vừa tạo
            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // Lưu vào bảng OrderDetails
            for (OrderDetail detail : order.getOrderDetails()) {
                PreparedStatement psDetail = conn.prepareStatement(insertOrderDetailSQL);
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, detail.getProductID());
                psDetail.setInt(3, detail.getQuantity());
                psDetail.setDouble(4, detail.getUnitPrice());
                psDetail.setFloat(5, detail.getDiscount());
                psDetail.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException | ClassNotFoundException ex) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi

                }
            } catch (SQLException rollbackEx) {
                Logger.getLogger(OrderDAO.class
                        .getName()).log(Level.SEVERE, "Rollback failed", rollbackEx);

            }
            Logger.getLogger(OrderDAO.class
                    .getName()).log(Level.SEVERE, "Error saving order", ex);
            return false;
        }
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrders(int orderId) {
        String deleteDetailsSql = "DELETE FROM OrderDetails WHERE OrderID = ?";
        String deleteOrderSql = "DELETE FROM Orders WHERE OrderID = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Xóa chi tiết đơn hàng trước
            try ( PreparedStatement psDetails = conn.prepareStatement(deleteDetailsSql)) {
                psDetails.setInt(1, orderId);
                psDetails.executeUpdate();
            }

            // Xóa đơn hàng
            try ( PreparedStatement psOrder = conn.prepareStatement(deleteOrderSql)) {
                psOrder.setInt(1, orderId);
                int rowsAffected = psOrder.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE OrderID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                order.setAccountName(rs.getString("AccountName"));
                order.setPayMentMethodName(rs.getString("PayMentMethodName"));
                order.setShipingMethodName(rs.getString("ShipingMethodName"));
                order.setOrderDate(rs.getTimestamp("OrderDate"));
                order.setStatus(rs.getString("Status"));
                order.setTotalAmount(rs.getDouble("TotalAmount"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                // Lấy thêm OrderDetails nếu cần
                return order;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order getOrderByIds(int orderId) {
        String sql = "SELECT o.OrderID, o.OrderDate, o.TotalAmount, o.Status, o.CreatedAt, "
                + "a.AccountName, p.MethodName AS PaymentMethodName, s.MethodName AS ShippingMethodName "
                + "FROM [dbo].[Orders] o "
                + "INNER JOIN [dbo].[Account] a ON o.AccountID = a.AccountID "
                + "INNER JOIN [dbo].[PaymentMethods] p ON o.PaymentMethodID = p.PaymentMethodID "
                + "INNER JOIN [dbo].[ShippingMethods] s ON o.ShippingMethodID = s.ShippingMethodID "
                + "WHERE o.OrderID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                order.setAccountName(rs.getString("AccountName"));
                order.setPayMentMethodName(rs.getString("PaymentMethodName"));
                order.setShipingMethodName(rs.getString("ShippingMethodName"));
                order.setOrderDate(rs.getTimestamp("OrderDate"));
                order.setStatus(rs.getString("Status"));
                order.setTotalAmount(rs.getDouble("TotalAmount"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return order;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
