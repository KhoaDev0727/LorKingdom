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

    private static String SHOW_QUANTITY_STOCK = "SELECT Quantity  FROM Product WHERE ProductID = ?";
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

    public static int getStockQuantity(int productId) throws SQLException, ClassNotFoundException {
        int stockQuantity = 0;
        conn = DBConnection.getConnection();
        try {
            stm = conn.prepareStatement(SHOW_QUANTITY_STOCK);
            stm.setInt(1, productId);
            rs = stm.executeQuery();
            if (rs.next()) {
                stockQuantity = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockQuantity;
    }

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

    public List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();

        // JOIN tất cả các bảng liên quan, 
        // đồng thời kiểm tra IsDeleted = 0 ở mọi bảng + p.Quantity > 0
        String query = "SELECT p.* "
                + "FROM Product p "
                + "JOIN Category c ON p.CategoryID = c.CategoryID "
                + "JOIN SuperCategory sc ON c.SuperCategoryID = sc.SuperCategoryID "
                + "JOIN Age a ON p.AgeID = a.AgeID "
                + "JOIN Brand b ON p.BrandID = b.BrandID "
                + "JOIN Material m ON p.MaterialID = m.MaterialID "
                + "JOIN PriceRange pr ON p.PriceRangeID = pr.PriceRangeID "
                + "JOIN Sex s ON p.SexID = s.SexID "
                + "JOIN Origin o ON p.OriginID = o.OriginID "
                + // Thêm JOIN Origin
                "WHERE p.IsDeleted = 0 "
                + "  AND c.IsDeleted = 0 "
                + "  AND sc.IsDeleted = 0 "
                + "  AND a.IsDeleted = 0 "
                + "  AND b.IsDeleted = 0 "
                + "  AND m.IsDeleted = 0 "
                + "  AND pr.IsDeleted = 0 "
                + "  AND s.IsDeleted = 0 "
                + "  AND o.IsDeleted = 0 "
                + "  AND p.Quantity > 0";    // Chỉ hiển thị sản phẩm có số lượng > 0

        try (
                 Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
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

                // Nếu bạn có thêm cột mainImageUrl trong bảng Product
                // product.setMainImageUrl(rs.getString("mainImageUrl"));
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
//

    public List<Product> getFilteredProducts(
            Integer categoryID,
            Integer ageID,
            Integer sexID,
            Integer priceRangeID,
            Integer brandID
    ) throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT p.* "
                + "FROM Product p "
                + "JOIN Category c ON p.CategoryID = c.CategoryID "
                + "JOIN SuperCategory sc ON c.SuperCategoryID = sc.SuperCategoryID "
                + "JOIN Age a ON p.AgeID = a.AgeID "
                + "JOIN Brand b ON p.BrandID = b.BrandID "
                + "JOIN Material m ON p.MaterialID = m.MaterialID "
                + "JOIN PriceRange pr ON p.PriceRangeID = pr.PriceRangeID "
                + "JOIN Sex s ON p.SexID = s.SexID "
                + "JOIN Origin o ON p.OriginID = o.OriginID "
                + "WHERE p.IsDeleted = 0 "
                + "  AND c.IsDeleted = 0 "
                + "  AND sc.IsDeleted = 0 "
                + "  AND a.IsDeleted = 0 "
                + "  AND b.IsDeleted = 0 "
                + "  AND m.IsDeleted = 0 "
                + "  AND pr.IsDeleted = 0 "
                + "  AND s.IsDeleted = 0 "
                + "  AND o.IsDeleted = 0 "
                + "  AND p.Quantity > 0"
        );
        List<Object> params = new ArrayList<>();

        if (categoryID != null) {
            query.append(" AND p.CategoryID = ?");
            params.add(categoryID);
        }
        if (priceRangeID != null) {
            query.append(" AND p.PriceRangeID = ?");
            params.add(priceRangeID);
        }
        if (ageID != null) {
            query.append(" AND p.AgeID = ?");
            params.add(ageID);
        }
        if (brandID != null) {
            query.append(" AND p.BrandID = ?");
            params.add(brandID);
        }
        if (sexID != null) {
            query.append(" AND p.SexID = ?");
            params.add(sexID);
        }

        products = executeQuery(query.toString(), params);
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

// (Tùy chọn) Tìm kiếm sản phẩm theo từ khóa
   public List<Product> searchProducts(String keyword) throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.* FROM Product p "
                + "JOIN Category c ON p.CategoryID = c.CategoryID "
                + "WHERE p.IsDeleted = 0 AND c.IsDeleted = 0 "
                + "AND (LOWER(p.Name) LIKE ? OR LOWER(p.SKU) LIKE ?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKey = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
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
                exists = rs.getInt(1) > 1; // true nếu đếm được > 0 dòng
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return exists;
    }

    public int getTotalProductsCount() throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM Product WHERE IsDeleted = 0";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Product> getProductsByPage(int page, int pageSize) throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String query = "SELECT * FROM Product WHERE IsDeleted = 0 ORDER BY CreatedAt ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
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
                    product.setIsDeleted(rs.getInt("IsDeleted"));
                    products.add(product);
                }
            }
        }
        return products;
    }

    public List<Product> getRelatedProductsByCategory(int categoryID, int excludeProductID, int limit) throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Product "
                + "WHERE IsDeleted = 0 "
                + "  AND CategoryID = ? "
                + "  AND ProductID <> ? " // để không lấy chính sản phẩm đang xem
                + "ORDER BY NEWID()";       // random, hoặc ORDER BY CreatedAt DESC tuỳ ý

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);         // TOP (limit)
            ps.setInt(2, categoryID);
            ps.setInt(3, excludeProductID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setSKU(rs.getString("SKU"));
                    p.setName(rs.getString("Name"));
                    p.setPrice(rs.getDouble("Price"));
                    // ... map các cột khác nếu cần
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean updateProductQuantity(int productId, int quantityToSubtract) throws SQLException, ClassNotFoundException {
            String sql = "UPDATE Product SET Quantity = Quantity - ? WHERE ProductID = ? AND Quantity >= ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityToSubtract);
            ps.setInt(2, productId);
            ps.setInt(3, quantityToSubtract); // Đảm bảo số lượng còn lại không âm
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
    

    public boolean revertStockOnCancel(int productId, int quantityToAdd) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET Quantity = Quantity + ? WHERE ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Bắt đầu transaction
            ps.setInt(1, quantityToAdd);
            ps.setInt(2, productId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit(); // Xác nhận thay đổi nếu thành công
                return true;
            } else {
                conn.rollback(); // Không có hàng nào bị ảnh hưởng, rollback
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi hoàn lại số lượng sản phẩm: " + e.getMessage(), e);
        }
    }

    // Phương thức lấy thông tin sản phẩm (nếu cần kiểm tra trước khi trừ)
    public Product getProductByID(int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE ProductID = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setName(rs.getString("Name"));
                product.setQuantity(rs.getInt("Quantity")); // Giả sử có cột Quantity
                // Các thuộc tính khác của Product nếu cần
                return product;
            }
        }
        return null;
    }

}
