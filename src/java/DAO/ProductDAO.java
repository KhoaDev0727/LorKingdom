/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

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
    private static String SELECT_PRODUCT_BY_ID ="SELECT * FROM Product WHERE ProductID = ?";
    private static String DELETE_REVIEW_BY_ID = "DELETE FROM Review WHERE ReviewID = ?";
    private static String ADD_PRODUCT_IMAGE = "INSERT INTO ProductImages (ProductID, Image, IsMain) VALUES (?, ?, ?)";

    private static String SELECT_ALL_PRODUCTS = "SELECT * FROM Product";
    // Câu lệnh SQL cho xóa, sửa sản phẩm
    private static String DELETE_PRODUCT = "DELETE FROM Product WHERE ProductID = ?";
    private static String UPDATE_PRODUCT = "UPDATE Product SET SKU = ?, CategoryID = ?, MaterialID = ?, "
            + "AgeID = ?, SexID = ?, PriceRangeID = ?, BrandID = ?, OriginID = ?, Name = ?, Price = ?, Quantity = ?, "
            + "Description = ? WHERE ProductID = ?";

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
// Phương thức xóa sản phẩm theo ProductID

    public static boolean deleteProduct(int productId) throws ClassNotFoundException {
        boolean deleted = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(DELETE_PRODUCT);
            stm.setInt(1, productId);
            deleted = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    // Phương thức sửa thông tin sản phẩm
    public static boolean updateProduct(Product p) throws ClassNotFoundException {
        boolean updated = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(UPDATE_PRODUCT);
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
            stm.setInt(13, p.getProductID());
            updated = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    // Phương thức lấy danh sách tất cả các sản phẩm
    public static List<Product> getAllProducts() throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_ALL_PRODUCTS);
            rs = stm.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getString("SKU"),
                        rs.getInt("CategoryID"),
                        rs.getInt("MaterialID"),
                        rs.getInt("AgeID"),
                        rs.getInt("SexID"),
                        rs.getInt("PriceRangeID"),
                        rs.getInt("BrandID"),
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Description")
                );
                p.setProductID(rs.getInt("ProductID"));
                // Nếu cần set các thuộc tính khác như status, createdAt, updatedAt, hãy bổ sung ở đây.
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getFilteredProducts(Integer categoryID, Integer ageID, Integer sexID, Integer priceRangeID, Integer brandID, Integer materialID, Integer originID) throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();

            // Câu lệnh SQL động
            StringBuilder query = new StringBuilder("SELECT * FROM Product WHERE 1=1");

            // Danh sách tham số
            List<Object> params = new ArrayList<>();

            if (categoryID != null) {
                query.append(" AND CategoryID = ?");
                params.add(categoryID);
            }
            if (ageID != null) {
                query.append(" AND AgeID = ?");
                params.add(ageID);
            }
            if (sexID != null) {
                query.append(" AND SexID = ?");
                params.add(sexID);
            }
            if (priceRangeID != null) {
                query.append(" AND PriceRangeID = ?");
                params.add(priceRangeID);
            }
            if (brandID != null) {
                query.append(" AND BrandID = ?");
                params.add(brandID);
            }
            if (materialID != null) {
                query.append(" AND MaterialID = ?");
                params.add(materialID);
            }
            if (originID != null) {
                query.append(" AND OriginID = ?");
                params.add(originID);
            }

            // Chuẩn bị truy vấn
            stm = conn.prepareStatement(query.toString());

            // Gán giá trị cho tham số
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            rs = stm.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getString("SKU"),
                        rs.getInt("CategoryID"),
                        rs.getInt("MaterialID"),
                        rs.getInt("AgeID"),
                        rs.getInt("SexID"),
                        rs.getInt("PriceRangeID"),
                        rs.getInt("BrandID"),
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Description")
                );
                p.setProductID(rs.getInt("ProductID"));
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static Product getProductById(int productId) throws ClassNotFoundException {
        Product product = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM Product WHERE ProductID = ?";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, productId);
            rs = stm.executeQuery();
            if (rs.next()) {
                product = new Product(
                        rs.getString("SKU"),
                        rs.getInt("CategoryID"),
                        rs.getInt("MaterialID"),
                        rs.getInt("AgeID"),
                        rs.getInt("SexID"),
                        rs.getInt("PriceRangeID"),
                        rs.getInt("BrandID"),
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Description")
                );
                product.setProductID(rs.getInt("ProductID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public Product getProduct(int id) {
        Product p = null;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_PRODUCT_BY_ID);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            while (rs.next()) {
                p = new Product(rs.getString("SKU"),
                        rs.getInt("CategoryID"),
                        rs.getInt("MaterialID"),
                        rs.getInt("AgeID"),
                        rs.getInt("SexID"),
                        rs.getInt("PriceRangeID"),
                        rs.getInt("BrandID"),
                        rs.getInt("OriginID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Description"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

}
