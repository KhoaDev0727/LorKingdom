/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.ProductImageDAO.conn;
import static DAO.ProductImageDAO.stm;
import DBConnect.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Product;
import java.sql.Statement;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class ProductDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    private static String SELECT_PRODUCT = "SELECT * FROM Review";
    private static String ADD_PRODUCT = "INSERT INTO Product ( SKU, CategoryID, MaterialID, AgeID, SexID, "
            + "PriceRangeID, BrandID, OriginID, Name, Price, Quantity, "
            + " Description ) VALUES ( ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static String DELETE_REVIEW_BY_ID = "DELETE FROM Review WHERE ReviewID = ?";
    private static String ADD_PRODUCT_IMAGE = "INSERT INTO ProductImages (ProductID, Image, IsMain) VALUES (?, ?, ?)";

    public static boolean addProduct(Product p, List<String> imagePaths, int isMain) throws ClassNotFoundException {
        boolean addRowProduct = false;
        boolean addRowProductImage = false;
        int productId = 0;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            // Thêm sản phẩm
            stm = conn.prepareStatement(ADD_PRODUCT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, p.getSKU());
            stm.setInt(2, p.getCategoryID());
            stm.setInt(3, p.getMaterialID());
            stm.setInt(4, p.getAgeID());
            stm.setInt(5, p.getSexID());
            stm.setInt(6, p.getPriceRangeID());
            stm.setInt(7, p.getBrandID());
            stm.setInt(8, p.getOriginID());
            stm.setString(9, p.getName());
            stm.setDouble(10, p.getPrice());
            stm.setInt(11, p.getQuantity());
            stm.setString(12, p.getDescription());
            addRowProduct = stm.executeUpdate() > 0;

            // Lấy productId vừa thêm
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                productId = rs.getInt(1);
            }

            // Thêm ảnh sản phẩm
            stm = conn.prepareStatement(ADD_PRODUCT_IMAGE);
            for (int i = 0; i < imagePaths.size(); i++) {
                stm.setInt(1, productId);
                stm.setString(2, imagePaths.get(i));
                stm.setInt(3, (i == 0) ? isMain : 0); // Ảnh đầu tiên là ảnh chính, còn lại là ảnh phụ
                stm.addBatch();
            }
            int[] rows = stm.executeBatch();

            // Kiểm tra tất cả các ảnh có được thêm thành công hay không
            addRowProductImage = true;
            for (int row : rows) {
                if (row <= 0) {
                    addRowProductImage = false;
                    break;
                }
            }

            // Xác nhận transaction
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Nếu lỗi thì rollback
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            e.printStackTrace();
        }
        return addRowProduct && addRowProductImage;
    }

}
