/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnect.DBConnection;
import Model.Order;
import Model.Product;
import Model.Promotion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */
public class PromotionDAO extends DBConnect.DBConnection {

    public List<Promotion> getAllPromotionsPerPage(int page, int proPerPage) throws SQLException, ClassNotFoundException {
        List<Promotion> list = new ArrayList<>();

        // Ensure the page number is greater than 0
        if (page < 1) {
            page = 1;
        }

        String query = "SELECT * FROM [dbo].[Promotions] p "
                + "ORDER BY p.PromotionID "
                + "OFFSET " + ((page - 1) * proPerPage) + " ROWS "
                + "FETCH NEXT " + proPerPage + " ROWS ONLY;";

        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Promotion(rs.getInt("PromotionID"),
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getDouble("DiscountPercent"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Status"),
                        rs.getDate("CreatedAt"),
                        rs.getDate("UpdatedAt"),
                        rs.getString("PromotionCode")));
            }
        }
        return list;
    }

    public int getTotalPromotion() throws SQLException, ClassNotFoundException {
        int total = 0;
        String query = "select count(*) from [dbo].[Promotions]";
        try ( Connection conn = DBConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void deletePromotion(int proId) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM [dbo].[Promotions]\n"
                + "WHERE PromotionID = ?;";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, proId);

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

    public List<Promotion> searchProByName(String proName) throws SQLException, ClassNotFoundException {
        List<Promotion> list = new ArrayList<>();
        String query = "select * from [dbo].[Promotions] where Name like ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + proName + "%");
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Promotion(rs.getInt("PromotionID"),
                            rs.getInt("ProductID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getDouble("DiscountPercent"),
                            rs.getDate("StartDate"),
                            rs.getDate("EndDate"),
                            rs.getString("Status"),
                            rs.getDate("CreatedAt"),
                            rs.getDate("UpdatedAt"),
                            rs.getString("PromotionCode")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPromotion(Promotion pro) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO [dbo].[Promotions] "
                + "([ProductID], [Name], [Description], [DiscountPercent], [StartDate], [EndDate], [Status], [CreatedAt], [UpdatedAt], [PromotionCode]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, pro.getProductID());
            pstmt.setString(2, pro.getName());
            pstmt.setString(3, pro.getDescription());
            pstmt.setDouble(4, pro.getDiscountPercent());
            pstmt.setDate(5, new java.sql.Date(pro.getStartDate().getTime()));
            pstmt.setDate(6, new java.sql.Date(pro.getEndDate().getTime()));
            pstmt.setString(7, pro.getStatus());
            pstmt.setDate(8, new java.sql.Date(pro.getCreatedAt().getTime()));
            pstmt.setDate(9, new java.sql.Date(pro.getUpdatedAt().getTime()));
            pstmt.setString(10, pro.getPromotionCode());

            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted); // Debug xem có bao nhiêu dòng được thêm

            return rowsInserted > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi SQL: " + e.getMessage()); // Ném lỗi để servlet nhận biết
        }
    }

    public List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT ProductID, name FROM Product WHERE IsDeleted = 0";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setName(rs.getString("name"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Promotion getById(int promotionId) throws SQLException, ClassNotFoundException {
        Promotion pro = null;
        String sql = "SELECT * FROM [dbo].[Promotions] WHERE PromotionID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, promotionId); // Đặt giá trị cho tham số

            try ( ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn
                if (rs.next()) {
                    pro = new Promotion();
                    pro.setPromotionID(rs.getInt("PromotionID"));
                    pro.setProductID(rs.getInt("ProductID"));
                    pro.setName(rs.getString("Name"));
                    pro.setDescription(rs.getString("Description"));
                    pro.setDiscountPercent(rs.getDouble("DiscountPercent"));
                    pro.setStartDate(rs.getDate("StartDate"));
                    pro.setEndDate(rs.getDate("EndDate"));
                    pro.setStatus(rs.getString("Status"));
                    pro.setCreatedAt(rs.getDate("CreatedAt"));
                    pro.setUpdatedAt(rs.getDate("UpdatedAt"));
                    pro.setPromotionCode(rs.getString("PromotionCode"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pro;
    }

    public void updatePromotion(Promotion promotion) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [dbo].[Promotions] SET ProductID=?, Name=?, Description=?, DiscountPercent=?, StartDate=?, EndDate=?, Status=?, PromotionCode=? WHERE PromotionID=?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, promotion.getProductID());
            ps.setString(2, promotion.getName());
            ps.setString(3, promotion.getDescription());
            ps.setDouble(4, promotion.getDiscountPercent());
            ps.setDate(5, new java.sql.Date(promotion.getStartDate().getTime()));
            ps.setDate(6, new java.sql.Date(promotion.getEndDate().getTime()));
            ps.setString(7, promotion.getStatus());
            ps.setString(8, promotion.getPromotionCode()); // Thêm dòng này để cập nhật PromotionCode
            ps.setInt(9, promotion.getPromotionID());

            int rowsUpdated = ps.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        }
    }

    // Kiểm tra xem promotion có tồn tại với cùng tên và % giảm giá hay không
    public boolean isPromotionExist(String name, double discountPercent) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Promotions WHERE name = ? AND discountPercent = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, discountPercent);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Kiểm tra xem có promotion nào trùng tên nhưng khác % giảm giá không
    public boolean isPromotionWithNameExist(String name) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Promotions WHERE name = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public List<Promotion> searchPromotionByDiscount(double minDiscount) throws SQLException, ClassNotFoundException {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM Promotions WHERE discountPercent < ? ORDER BY discountPercent DESC";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minDiscount);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setPromotionID(rs.getInt("PromotionID"));
                promotion.setProductID(rs.getInt("ProductID"));
                promotion.setName(rs.getString("Name"));
                promotion.setDescription(rs.getString("Description"));
                promotion.setDiscountPercent(rs.getDouble("DiscountPercent"));
                promotion.setStartDate(rs.getDate("StartDate"));
                promotion.setEndDate(rs.getDate("EndDate"));
                promotion.setStatus(rs.getString("Status"));
                promotion.setCreatedAt(rs.getDate("CreatedAt"));
                promotion.setUpdatedAt(rs.getDate("UpdatedAt"));
                promotion.setPromotionCode(rs.getString("PromotionCode"));

                promotions.add(promotion);
            }
        }
        return promotions;
    }

    public boolean isPromotionExistForUpdate(int promotionID, String name, double discountPercent) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Promotions WHERE name = ? AND discountPercent = ? AND PromotionID <> ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, discountPercent);
            ps.setInt(3, promotionID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public List<Promotion> getPromotionsByProductId(int productId) throws SQLException, ClassNotFoundException {
        List<Promotion> list = new ArrayList<>();
        String query = "SELECT * FROM [dbo].[Promotions] WHERE ProductID = ? AND Status = 'Active' AND EndDate >= GETDATE()";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Promotion(
                            rs.getInt("PromotionID"),
                            rs.getInt("ProductID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getDouble("DiscountPercent"),
                            rs.getDate("StartDate"),
                            rs.getDate("EndDate"),
                            rs.getString("Status"),
                            rs.getDate("CreatedAt"),
                            rs.getDate("UpdatedAt"),
                            rs.getString("PromotionCode")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void updateExpiredPromotions() throws SQLException, ClassNotFoundException {
    String query = "UPDATE [dbo].[Promotions] SET Status = 'Expired' WHERE Status = 'Active' AND EndDate < ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        // Lấy ngày hiện tại
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        // Đặt giá trị cho tham số
        pstmt.setDate(1, currentDate);

        // Thực thi câu lệnh UPDATE
        int rowsUpdated = pstmt.executeUpdate();
        System.out.println("Rows updated to Expired: " + rowsUpdated);
    } catch (SQLException e) {
        e.printStackTrace();
        throw new SQLException("Lỗi khi cập nhật trạng thái khuyến mãi: " + e.getMessage());
    }
}
    
        public void updatePromotionStatusForDeletedProducts() throws SQLException, ClassNotFoundException {
        String query = "UPDATE [dbo].[Promotions] "
                + "SET Status = 'Expired', UpdatedAt = ? "
                + "WHERE ProductID IN (SELECT ProductID FROM Product WHERE IsDeleted = 1) "
                + "AND Status != 'Expired'";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the current date as the UpdatedAt value
            pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated to Expired for deleted products: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating promotion status for deleted products: " + e.getMessage());
        }
    }

    public void updatePromotionStatusForRestoredProducts() throws SQLException, ClassNotFoundException {
        String query = "UPDATE [dbo].[Promotions] "
                + "SET Status = 'Active', UpdatedAt = ? "
                + "WHERE ProductID IN (SELECT ProductID FROM Product WHERE IsDeleted = 0) "
                + "AND Status = 'Expired' "
                + "AND EndDate >= ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            pstmt.setDate(1, currentDate); // UpdatedAt = ngày hiện tại
            pstmt.setDate(2, currentDate); // EndDate >= ngày hiện tại

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated to Active for restored products: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating promotion status for restored products: " + e.getMessage());
        }
    }

}
