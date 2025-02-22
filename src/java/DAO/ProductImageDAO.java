/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.AccountDAO.conn;
import static DAO.AccountDAO.stm;
import DBConnect.DBConnection;
import Model.Account;
import Model.ProductImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class ProductImageDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;
    private static String ADD_PRODUCT_IMAGE = "INSERT INTO ProductImages (ProductID, Image, IsMain) VALUES (?, ?, ?)";
    private static String UPDATE_PRODUCT_IMAGE = "UPDATE ProductImages SET Image = ?, IsMain = ? WHERE ImageID = ?";
    private static String DELETE_PRODUCT_IMAGE = "DELETE FROM ProductImages WHERE ImageID = ?";
    private static String SEARCH_PRODUCT_IMAGE_BY_PRODUCTID = "SELECT * FROM ProductImages WHERE ProductID = ?";
    private static String SEARCH_PRODUCT_IMAGE_BY_ID = "SELECT * FROM ProductImages WHERE ImageID = ?";

    public static Boolean addProductImage(int productID, String mainImagePath, int isMain) {
        boolean rowUpdate = false;
        try {
            stm = conn.prepareStatement(ADD_PRODUCT_IMAGE);
            stm.setInt(1, productID);
            stm.setString(2, mainImagePath);
            stm.setInt(3, isMain);
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }

        return rowUpdate;
    }
// Cập nhật thông tin ảnh sản phẩm theo ImageID

    public static boolean updateProductImage(int imageID, String imageUrl, int isMain) throws ClassNotFoundException {
        boolean updated = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(UPDATE_PRODUCT_IMAGE);
            stm.setString(1, imageUrl);
            stm.setInt(2, isMain);
            stm.setInt(3, imageID);
            updated = stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Lỗi khi cập nhật ảnh sản phẩm: " + ex);
        }
        return updated;
    }

    // Xóa ảnh sản phẩm theo ImageID
    public static boolean deleteProductImage(int imageID) throws ClassNotFoundException {
        boolean deleted = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(DELETE_PRODUCT_IMAGE);
            stm.setInt(1, imageID);
            deleted = stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Lỗi khi xóa ảnh sản phẩm: " + ex);
        }
        return deleted;
    }

    // Tìm kiếm danh sách ảnh theo ProductID
    public static List<ProductImage> searchProductImagesByProductId(String productID) throws ClassNotFoundException {
        List<ProductImage> images = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SEARCH_PRODUCT_IMAGE_BY_PRODUCTID);
            stm.setString(1, productID);
            rs = stm.executeQuery();
            while (rs.next()) {
                int imageID = rs.getInt("ImageID");
                String prodID = rs.getString("ProductID");
                String imageUrl = rs.getString("Image");
                int isMain = rs.getInt("IsMain");
                images.add(new ProductImage(imageID, prodID, imageUrl, isMain));
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi khi tìm kiếm ảnh sản phẩm theo ProductID: " + ex);
        }
        return images;
    }

    // Tìm kiếm ảnh sản phẩm theo ImageID
    public static ProductImage searchProductImageByID(int imageID) throws ClassNotFoundException {
        ProductImage image = null;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SEARCH_PRODUCT_IMAGE_BY_ID);
            stm.setInt(1, imageID);
            rs = stm.executeQuery();
            if (rs.next()) {
                String prodID = rs.getString("ProductID");
                String imageUrl = rs.getString("Image");
                int isMain = rs.getInt("IsMain");
                image = new ProductImage(imageID, prodID, imageUrl, isMain);
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi khi tìm kiếm ảnh sản phẩm theo ImageID: " + ex);
        }
        return image;
    }
    private static String GET_ALL_PRODUCT_IMAGES = "SELECT * FROM ProductImages";

    public static List<ProductImage> getAllProductImages() throws ClassNotFoundException {
        List<ProductImage> images = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(GET_ALL_PRODUCT_IMAGES);
            rs = stm.executeQuery();
            while (rs.next()) {
                int imageID = rs.getInt("ImageID");
                String prodID = rs.getString("ProductID");
                String imageUrl = rs.getString("Image");
                int isMain = rs.getInt("IsMain");
                images.add(new ProductImage(imageID, prodID, imageUrl, isMain));
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi khi lấy tất cả ảnh sản phẩm: " + ex);
        }
        return images;
    }

    public static List<ProductImage> getMainProductImages() throws ClassNotFoundException {
        List<ProductImage> images = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ProductImages WHERE IsMain = 1";
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                int imageID = rs.getInt("ImageID");
                String prodID = rs.getString("ProductID");
                String imageUrl = rs.getString("Image");
                int isMain = rs.getInt("IsMain");
                images.add(new ProductImage(imageID, prodID, imageUrl, isMain));
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi khi lấy ảnh chính: " + ex);
        }
        return images;
    }

    public static List<ProductImage> getSecondaryProductImagesByProductId(int productID) throws ClassNotFoundException {
        List<ProductImage> images = new ArrayList<>();
        String sql = "SELECT * FROM ProductImages WHERE ProductID = ? AND IsMain = 0";
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setInt(1, productID);
            rs = stm.executeQuery();
            while (rs.next()) {
                int imageID = rs.getInt("ImageID");
                String prodID = rs.getString("ProductID");
                String imageUrl = rs.getString("Image");
                int isMain = rs.getInt("IsMain");
                images.add(new ProductImage(imageID, prodID, imageUrl, isMain));
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi khi lấy ảnh phụ (IsMain=0) theo ProductID: " + ex);
        }
        return images;
    }

}
