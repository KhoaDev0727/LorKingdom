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
    private static String SELECT_PRODUCT_BY_ID = "SELECT * FROM Product WHERE ProductID = ?";
    private static String DELETE_REVIEW_BY_ID = "DELETE FROM Review WHERE ReviewID = ?";
    private static String ADD_PRODUCT_IMAGE = "INSERT INTO ProductImages (ProductID, Image, IsMain) VALUES (?, ?, ?)";

    private static String SELECT_ALL_PRODUCTS = "SELECT * FROM Product";
    // Câu lệnh SQL cho xóa, sửa sản phẩm
    private static String DELETE_PRODUCT = "DELETE FROM Product WHERE ProductID = ?";
    private static final String UPDATE_PRODUCT
            = "UPDATE Product "
            + "SET SKU = ?, "
            + "    CategoryID = ?, "
            + "    MaterialID = ?, "
            + "    AgeID = ?, "
            + "    SexID = ?, "
            + "    PriceRangeID = ?, "
            + "    BrandID = ?, "
            + "    OriginID = ?, "
            + "    Name = ?, "
            + "    Price = ?, "
            + "    Quantity = ?, "
            + "    Description = ? "
            + "WHERE ProductID = ?";

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

    public void softDeleteProduct(int productID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Product SET IsDeleted = 1 WHERE ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productID);
            ps.executeUpdate();
        }
    }

    // 6. Xóa cứng sản phẩm (DELETE khỏi bảng)
    public void hardDeleteProduct(int productID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Product WHERE ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productID);
            ps.executeUpdate();
        }
    }

    // 7. Khôi phục sản phẩm (set IsDeleted = 0)
    public void restoreProduct(int productID) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Product SET IsDeleted = 0 WHERE ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productID);
            ps.executeUpdate();
        }
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

            updated = (stm.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    // Phương thức lấy danh sách tất cả các sản phẩm
    public List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setSKU(rs.getString("SKU"));
                product.setCategoryID(rs.getObject("CategoryID") != null ? rs.getInt("CategoryID") : null);
                product.setMaterialID(rs.getObject("MaterialID") != null ? rs.getInt("MaterialID") : null);
                product.setAgeID(rs.getObject("AgeID") != null ? rs.getInt("AgeID") : null);
                product.setSexID(rs.getObject("SexID") != null ? rs.getInt("SexID") : null);
                product.setPriceRangeID(rs.getObject("PriceRangeID") != null ? rs.getInt("PriceRangeID") : null);
                product.setBrandID(rs.getObject("BrandID") != null ? rs.getInt("BrandID") : null);
                product.setOriginID(rs.getObject("OriginID") != null ? rs.getInt("OriginID") : null);
                product.setName(rs.getString("Name"));
                product.setPrice(rs.getDouble("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setStatus(rs.getString("Status"));
                product.setDescription(rs.getString("Description"));
                product.setCreatedAt(rs.getTimestamp("CreatedAt"));
                product.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                product.setIsDeleted(rs.getInt("IsDeleted"));

                // Nếu có cột mainImageUrl, có thể set thêm tại đây
                products.add(product);
            }
        }
        return products;
    }

    // 2. Lấy danh sách sản phẩm đã bị xóa mềm (IsDeleted = 1)
    public List<Product> getDeletedProducts() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE IsDeleted = 1";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setSKU(rs.getString("SKU"));
                product.setCategoryID(rs.getObject("CategoryID") != null ? rs.getInt("CategoryID") : null);
                product.setMaterialID(rs.getObject("MaterialID") != null ? rs.getInt("MaterialID") : null);
                product.setAgeID(rs.getObject("AgeID") != null ? rs.getInt("AgeID") : null);
                product.setSexID(rs.getObject("SexID") != null ? rs.getInt("SexID") : null);
                product.setPriceRangeID(rs.getObject("PriceRangeID") != null ? rs.getInt("PriceRangeID") : null);
                product.setBrandID(rs.getObject("BrandID") != null ? rs.getInt("BrandID") : null);
                product.setOriginID(rs.getObject("OriginID") != null ? rs.getInt("OriginID") : null);
                product.setName(rs.getString("Name"));
                product.setPrice(rs.getDouble("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setStatus(rs.getString("Status"));
                product.setDescription(rs.getString("Description"));
                product.setCreatedAt(rs.getTimestamp("CreatedAt"));
                product.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                product.setIsDeleted(rs.getInt("IsDeleted"));
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> getFilteredProducts(Integer categoryID, Integer ageID, Integer sexID,
            Integer priceRangeID, Integer brandID, Integer materialID, Integer originID) throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        // Xây dựng truy vấn ban đầu với tất cả các điều kiện
        StringBuilder query = new StringBuilder("SELECT * FROM Product WHERE 1=1");
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

        products = executeQuery(query.toString(), params);

        if (products.isEmpty()) {
            StringBuilder fallbackQuery = new StringBuilder("SELECT * FROM Product WHERE IsDeleted = 0 AND (");
            List<Object> fallbackParams = new ArrayList<>();
            boolean firstCondition = true;

            if (categoryID != null) {
                fallbackQuery.append(" CategoryID = ? ");
                fallbackParams.add(categoryID);
                firstCondition = false;
            }
            if (ageID != null) {
                if (!firstCondition) {
                    fallbackQuery.append(" OR ");
                }
                fallbackQuery.append(" AgeID = ? ");
                fallbackParams.add(ageID);
                firstCondition = false;
            }
            if (sexID != null) {
                if (!firstCondition) {
                    fallbackQuery.append(" OR ");
                }
                fallbackQuery.append(" SexID = ? ");
                fallbackParams.add(sexID);
                firstCondition = false;
            }
            if (priceRangeID != null) {
                if (!firstCondition) {
                    fallbackQuery.append(" OR ");
                }
                fallbackQuery.append(" PriceRangeID = ? ");
                fallbackParams.add(priceRangeID);
                firstCondition = false;
            }
            if (brandID != null) {
                if (!firstCondition) {
                    fallbackQuery.append(" OR ");
                }
                fallbackQuery.append(" BrandID = ? ");
                fallbackParams.add(brandID);
                firstCondition = false;
            }
            if (materialID != null) {
                if (!firstCondition) {
                    fallbackQuery.append(" OR ");
                }
                fallbackQuery.append(" MaterialID = ? ");
                fallbackParams.add(materialID);
                firstCondition = false;
            }
            if (originID != null) {
                if (!firstCondition) {
                    fallbackQuery.append(" OR ");
                }
                fallbackQuery.append(" OriginID = ? ");
                fallbackParams.add(originID);
            }
            fallbackQuery.append(")");

            products = executeQuery(fallbackQuery.toString(), fallbackParams);
        }

        return products;
    }

    private List<Product> executeQuery(String query, List<Object> params) throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stm = conn.prepareStatement(query)) {

            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            try ( ResultSet rs = stm.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

////    public List<Product> getFilteredProducts(Integer categoryID, Integer ageID, Integer sexID, Integer priceRangeID, Integer brandID, Integer materialID, Integer originID) throws ClassNotFoundException {
////        List<Product> products = new ArrayList<>();
////        try {
////            conn = DBConnection.getConnection();
////
////            // Câu lệnh SQL động
////            StringBuilder query = new StringBuilder("SELECT * FROM Product WHERE 1=1");
////
////            // Danh sách tham số
////            List<Object> params = new ArrayList<>();
////
////            if (categoryID != null) {
////                query.append(" AND CategoryID = ?");
////                params.add(categoryID);
////            }
////            if (ageID != null) {
////                query.append(" AND AgeID = ?");
////                params.add(ageID);
////            }
////            if (sexID != null) {
////                query.append(" AND SexID = ?");
////                params.add(sexID);
////            }
////            if (priceRangeID != null) {
////                query.append(" AND PriceRangeID = ?");
////                params.add(priceRangeID);
////            }
////            if (brandID != null) {
////                query.append(" AND BrandID = ?");
////                params.add(brandID);
////            }
////            if (materialID != null) {
////                query.append(" AND MaterialID = ?");
////                params.add(materialID);
////            }
////            if (originID != null) {
////                query.append(" AND OriginID = ?");
////                params.add(originID);
////            }
////
////            // Chuẩn bị truy vấn
////            stm = conn.prepareStatement(query.toString());
////
////            // Gán giá trị cho tham số
////            for (int i = 0; i < params.size(); i++) {
////                stm.setObject(i + 1, params.get(i));
////            }
////
////            rs = stm.executeQuery();
////            while (rs.next()) {
////                Product p = new Product(
////                        rs.getString("SKU"),
////                        rs.getInt("CategoryID"),
////                        rs.getInt("MaterialID"),
////                        rs.getInt("AgeID"),
////                        rs.getInt("SexID"),
////                        rs.getInt("PriceRangeID"),
////                        rs.getInt("BrandID"),
////                        rs.getInt("OriginID"),
////                        rs.getString("Name"),
////                        rs.getDouble("Price"),
////                        rs.getInt("Quantity"),
////                        rs.getString("Description")
////                );
////                p.setProductID(rs.getInt("ProductID"));
////                products.add(p);
////            }
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        return products;
////    }
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
// (Tùy chọn) Tìm kiếm sản phẩm theo từ khóa

    public List<Product> searchProducts(String keyword) throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE Name LIKE ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setSKU(rs.getString("SKU"));
                    product.setCategoryID(rs.getObject("CategoryID") != null ? rs.getInt("CategoryID") : null);
                    product.setMaterialID(rs.getObject("MaterialID") != null ? rs.getInt("MaterialID") : null);
                    product.setAgeID(rs.getObject("AgeID") != null ? rs.getInt("AgeID") : null);
                    product.setSexID(rs.getObject("SexID") != null ? rs.getInt("SexID") : null);
                    product.setPriceRangeID(rs.getObject("PriceRangeID") != null ? rs.getInt("PriceRangeID") : null);
                    product.setBrandID(rs.getObject("BrandID") != null ? rs.getInt("BrandID") : null);
                    product.setOriginID(rs.getObject("OriginID") != null ? rs.getInt("OriginID") : null);
                    product.setName(rs.getString("Name"));
                    product.setPrice(rs.getDouble("Price"));
                    product.setQuantity(rs.getInt("Quantity"));
                    product.setStatus(rs.getString("Status"));
                    product.setDescription(rs.getString("Description"));
                    product.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    product.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    products.add(product);
                }
            }
        }
        return products;
    }

    public static boolean isProductNameExists(String productName, int excludeProductID)
            throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM Product WHERE Name = ? AND ProductID <> ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, productName);
            ps.setInt(2, excludeProductID);
            rs = ps.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0; // true nếu đếm được > 0 dòng
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return exists;
    }

}
