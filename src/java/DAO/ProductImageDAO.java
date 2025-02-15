/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.AccountDAO.conn;
import static DAO.AccountDAO.stm;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class ProductImageDAO {
    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;
    private static String ADD_PRODUCT_IMAGE = "INSERT INTO ProductImages (ProductID, Image, IsMain) VALUES (?, ?, ?)";

    public static Boolean addProductImage(int productID, String mainImagePath, int isMain) {
        boolean rowUpdate = false;
        try {
            stm = conn.prepareStatement(ADD_PRODUCT_IMAGE);
            stm.setInt(1,productID);
            stm.setString(2,mainImagePath);
            stm.setInt(3,isMain);
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }

        return rowUpdate;
    }

}
