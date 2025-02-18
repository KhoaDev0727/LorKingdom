/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Order;
import Model.OrderDetail;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */
public class OrderDetailDAO extends DBConnect.DBConnection {

    public List<OrderDetail> getOrderDetailById(int orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetail> list = new ArrayList<>();
        String query = "select od.*, p.Name from [dbo].[OrderDetails] od join [dbo].[Product] p \n"
                + "on od.ProductID = p.ProductID\n"
                + "where od.OrderID =  " + orderId;
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String productName = rs.getString("Name");
                list.add(new OrderDetail(rs.getInt("OrderDetailID"),
                        rs.getInt("OrderID"),
                        productName,
                        rs.getInt("Quantity"),
                        rs.getFloat("UnitPrice"),
                        rs.getFloat("Discount")));

            }
        }
        return list;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try {
            OrderDetailDAO dao = new OrderDetailDAO();
            List<OrderDetail> list = dao.getOrderDetailById(4);
            for (OrderDetail orderDetail : list) {
                System.out.println(orderDetail);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
