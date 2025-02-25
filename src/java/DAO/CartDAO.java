/*
             * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
             * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.CartItems;
import Model.Product;
import Model.ProductImage;
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
public class CartDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    protected static String DELETE_ITEM = "DELETE FROM Cart WHERE AccountID = ? AND ProductID = ?";
    protected static String ADD_ITEM = "INSERT INTO Cart (AccountID, ProductID, Quantity) VALUES (?, ?, ?, ?)";
    protected static String UPDATE_ITEM = "UPDATE Cart SET Quantity  = ? WHERE AccountID  = ? AND ProductID  = ?";
    protected static String DELETE_ALL_ITEM = "DELETE FROM Cart WHERE AccountID = ?";

    public static boolean addItem(int userId, int productId, int quantity) throws SQLException {
        boolean rowUpdate = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(ADD_ITEM);
            stm.setInt(1, userId);
            stm.setInt(2, productId);
            stm.setInt(3, quantity);
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowUpdate;
    }

    public static boolean updateItem(int userId, int productId, int quantity) throws SQLException {
        boolean updateRow = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(UPDATE_ITEM);
            stm.setInt(1, quantity);
            stm.setInt(2, userId);
            stm.setInt(3, productId);
            updateRow = stm.executeUpdate() > 0;
        } catch (Exception e) {
        }

        return updateRow;
    }

    public static boolean removeItem(int userId, int productId) throws SQLException {
        boolean updateRow = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(DELETE_ITEM);
            stm.setInt(1, userId);
            stm.setInt(2, productId);
            updateRow = stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateRow;
    }

    public static boolean removeAll(int userId) throws SQLException {
        boolean updateRow = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(DELETE_ALL_ITEM );
            stm.setInt(1, userId);
            updateRow = stm.executeUpdate() > 0;
        } catch (Exception e) {
        }
        return updateRow;
    }

    public List<CartItems> getCartItems(int userId) throws SQLException {
        List<CartItems> items = new ArrayList<>();
        String query = "SELECT c.ProductID, c.Quantity, p.Price, p.Name "
                + "FROM Cart c JOIN Product p ON c.ProductID = p.ProductID "
                + "WHERE c.AccountID = ?";
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(query);
            stm.setInt(1, userId);
            rs = stm.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID"));
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getDouble("Price")
                );
               
                ProductImage mainImage = ProductImageDAO.getMainImage(product.getProductID());
                if (mainImage != null) {
                    product.setMainImageUrl(mainImage.getImageUrl());
                }
                CartItems item = new CartItems(product, rs.getDouble("Price"), rs.getInt("Quantity"));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
