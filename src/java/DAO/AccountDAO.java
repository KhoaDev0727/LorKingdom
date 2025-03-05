/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Controller.MyUtils;
import DBConnect.DBConnection;
import Model.Account;
import Model.Role;
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
public class AccountDAO {

    protected static String TestRole = "SELECT * FROM Role";
    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;
    private static String SELECT_BY_ROLE_CUSTOMER = "SELECT * FROM Account WHERE RoleID = ? ORDER BY AccountID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
    private static String SELECT_BY_ROLE_STAFF = "SELECT * FROM Account WHERE RoleID = ? OR RoleID =? OR RoleID = ? ORDER BY AccountID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
    private static String SELECT_NAME_ACCOUNT_BY_ID = "SELECT AccountID, AccountName, Image, Password  FROM Account WHERE AccountID = ?";
    private static String UPDATE_PROFILE = "UPDATE Account SET AccountName = ?, PhoneNumber = ?, Email ail = ?,Image = ?, Password = ?, Address = ?, Status = ?, Balance = ?, UpdatedAt = ? WHERE AccountID = ?";
    private static String UPDATE_PROFILE_STAFF = "UPDATE Account SET AccountName = ?, PhoneNumber = ?, Email = ?, Password = ?, Address = ?, Status = ?,Image = ?, UpdatedAt = ? WHERE AccountID = ?";
    private static String DELETE_ACCOUNT_SOFT = "UPDATE Account SET isDeleted = ?, Status= ? WHERE AccountID = ?";
    private static String DELETE_ACCOUNT_BY_ID = "DELETE FROM Account WHERE AccountID = ?";
    private static String SEARCH_ACCOUNT = "SELECT * FROM Account WHERE ( AccountID = ? OR AccountName LIKE ? OR PhoneNumber LIKE ? OR email LIKE ? ) AND RoleID = ? ORDER BY AccountID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
    private static String ADD_ADMIN = "INSERT INTO Account (AccountName, PhoneNumber, Email, Image, Password, Address, Status, RoleID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static String IS_EMAIL_EXIST = "SELECT COUNT(*) FROM Account WHERE Email = ?";

    public static boolean isEmailExists(String email) throws SQLException, ClassNotFoundException {
        conn = DBConnection.getConnection();
        try {
            stm = conn.prepareStatement(IS_EMAIL_EXIST);
            stm.setString(1, email);
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 1; // check email 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account getAccountByEmail(String email) {
        String query = "SELECT * FROM Account WHERE Email = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getInt("AccountID"),
                            rs.getString("AccountName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Address"),
                            rs.getString("Status"),
                            rs.getDouble("Balance"),
                            rs.getString("Image")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAddress(String email, String address) {
        String query = "UPDATE Account SET Address = ? WHERE Email = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, address);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updatePhoneNumber(String email, String phoneNumber) {
        String query = "UPDATE Account SET PhoneNumber = ? WHERE Email = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, phoneNumber);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Account getInforAccountByID(int AccountID) throws SQLException {
        Account account = new Account();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_NAME_ACCOUNT_BY_ID);
            stm.setInt(1, AccountID); // RoleID
            rs = stm.executeQuery();
            if (rs.next()) {
                account = new Account(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            } else {
                System.out.println("Không tìm thấy tài khoản với ID: " + AccountID);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return account;
    }

    public static List<Account> getAllAccountCustomer(int roleCustomer, int page, int pageSize) {
        List<Account> list = new ArrayList<>();
        try {
            if (page <= 0) {
                page = 1;
            }
            if (pageSize <= 0) {
                pageSize = 5; // Mặc định mỗi trang 5 bản ghi
            }
            int offset = (page - 1) * pageSize; // Tính offset

            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_BY_ROLE_CUSTOMER);
            stm.setInt(1, roleCustomer); // RoleID
            stm.setInt(2, offset); // OFFSET
            stm.setInt(3, pageSize); // FETCH NEXT

            rs = stm.executeQuery();
            while (rs.next()) {
                Account a = new Account(
                        rs.getInt(1), // 1. AccountID (INT)
                        rs.getInt(2), // 2. RoleID (INT, nullable)
                        rs.getString(3), // 3. AccountName (NVARCHAR)
                        rs.getString(4), // 4. PhoneNumber (NVARCHAR)
                        rs.getString(5), // 5. Email (NVARCHAR)
                        rs.getString(6), // 6. Image (NVARCHAR, nullable)
                        rs.getString(7), // 7. Password (NVARCHAR)
                        rs.getString(8), // 8. Address (NVARCHAR, nullable)
                        rs.getInt(9), // 9. IsDeleted (BIT)
                        rs.getString(10), // 10. Status (NVARCHAR)
                        rs.getDouble(11), // 11. Balance (DECIMAL)
                        rs.getTimestamp(12), // 12. CreatedAt (DATETIME)
                        rs.getTimestamp(13) // 13. UpdatedAt (DATETIME, nullable)
                );
                list.add(a);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public static List<Account> getAllAccountStaff(int roleStaff, int roleAdmin, int roleWareHouse, int page, int pageSize) {
        List<Account> list = new ArrayList<>();
        try {
            if (page <= 0) {
                page = 1;
            }
            if (pageSize <= 0) {
                pageSize = 5; // Mặc định mỗi trang 5 bản ghi
            }
            int offset = (page - 1) * pageSize; // Tính offset

            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_BY_ROLE_STAFF);
            stm.setInt(1, roleStaff); // RoleID
            stm.setInt(2, roleAdmin); // RoleID
            stm.setInt(3, roleWareHouse); // RoleID
            stm.setInt(4, offset); // OFFSET
            stm.setInt(5, pageSize); // FETCH NEXT

            rs = stm.executeQuery();
            while (rs.next()) {
                Account a = new Account(
                        rs.getInt(1), // 1. AccountID (INT)
                        rs.getInt(2), // 2. RoleID (INT, nullable)
                        rs.getString(3), // 3. AccountName (NVARCHAR)
                        rs.getString(4), // 4. PhoneNumber (NVARCHAR)
                        rs.getString(5), // 5. Email (NVARCHAR)
                        rs.getString(6), // 6. Image (NVARCHAR, nullable)
                        rs.getString(7), // 7. Password (NVARCHAR)
                        rs.getString(8), // 8. Address (NVARCHAR, nullable)
                        rs.getInt(9), // 9. IsDeleted (BIT)
                        rs.getString(10), // 10. Status (NVARCHAR)
                        rs.getDouble(11), // 11. Balance (DECIMAL)
                        rs.getTimestamp(12), // 12. CreatedAt (DATETIME)
                        rs.getTimestamp(13) // 13. UpdatedAt (DATETIME, nullable)
                );
                list.add(a);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public static Boolean updateProfile(Account account) {
        boolean rowUpdate = false;
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        try {
            stm = conn.prepareStatement(UPDATE_PROFILE);
            stm.setString(1, account.getUserName());
            stm.setString(2, account.getPhoneNumber());
            stm.setString(3, account.getEmail());
            stm.setString(4, account.getPassword());
            stm.setString(5, account.getAddress());
            stm.setString(6, account.getStatus());
            stm.setDouble(7, account.getBalance());
            stm.setString(8, account.getImage());
            stm.setTimestamp(9, currentTimestamp); // Lấy thời gian hiện tại
            stm.setInt(10, account.getAccountId()); // Gán ID tài khoản cần cập nhật
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowUpdate;
    }

    public static Boolean updateProfileStaff(Account account) {
        boolean rowUpdate = false;
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        try {

            stm = conn.prepareStatement(UPDATE_PROFILE_STAFF);
            stm.setString(1, account.getUserName());
            stm.setString(2, account.getPhoneNumber());
            stm.setString(3, account.getEmail());
            stm.setString(4, account.getPassword());
            stm.setString(5, account.getAddress());
            stm.setString(6, account.getStatus());
            stm.setString(7, account.getImage());
            stm.setTimestamp(8, currentTimestamp); // Lấy thời gian hiện tại
            stm.setInt(9, account.getAccountId()); // Gán ID tài khoản cần cập nhật
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowUpdate;
    }

    public static Boolean addAdmin(Account account) {
        boolean rowUpdate = false;
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        try {
            stm = conn.prepareStatement(ADD_ADMIN);
            stm.setString(1, account.getUserName());
            stm.setString(2, account.getPhoneNumber());
            stm.setString(3, account.getEmail());
            stm.setString(4, account.getImage());
            stm.setString(5, account.getPassword());
            stm.setString(6, account.getAddress());
            stm.setString(7, account.getStatus());
            stm.setInt(8, account.getRoleID()); // Gán ID tài khoản cần cập nhật
            rowUpdate = stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }

        return rowUpdate;
    }

    public static Boolean DeleteSoftAccountById(int accountId) {
        boolean rowUpdated = false;
        String status = "Inactive";
        try {
            stm = conn.prepareStatement(DELETE_ACCOUNT_SOFT);
            stm.setInt(1, 1);  // Đánh dấu isDeleted = 1 (Soft Delete)
            stm.setString(2, status); // Cập nhật trạng thái thành "Inactive"
            stm.setInt(3, accountId);  // WHERE AccountID = accountId
            rowUpdated = stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowUpdated;
    }
//
//    public static Boolean DeleteAccountById(int Id) {
//        boolean rowUpdated = false;
//        try {
//            stm = conn.prepareStatement(DELETE_ACCOUNT_BY_ID);
//            stm.setInt(1, Id);
//            rowUpdated = stm.executeUpdate() > 0;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return rowUpdated;
//    }

    public static List<Role> showListRoleTest() {
        List<Role> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TestRole);
            rs = stm.executeQuery();
            while (rs.next()) {
                Role a = new Role(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                );
                list.add(a);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public static List<Account> findUser(String keyword, int roleID, int page, int pageSize) {
        List<Account> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SEARCH_ACCOUNT);
            // Kiểm tra nếu keyword là số → tìm theo AccountID
            if (keyword.matches("\\d+")) {
                stm.setInt(1, Integer.parseInt(keyword)); // Tìm chính xác AccountID
            } else {
                stm.setNull(1, java.sql.Types.INTEGER); // Bỏ qua AccountID
            }
            // Dùng wildcard % để tìm kiếm tương đối
            String searchPattern = "%" + keyword + "%";
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);
            stm.setString(4, searchPattern);
            stm.setInt(5, roleID);
            // Calculate OFFSET and ROWS
            int offset = (page - 1) * pageSize;
            stm.setInt(6, offset);
            stm.setInt(7, pageSize);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account(
                            rs.getInt("AccountID"),
                            rs.getInt("RoleID"), // nullable
                            rs.getString("AccountName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Email"),
                            rs.getString("Image"), // nullable
                            rs.getString("Password"),
                            rs.getString("Address"), // nullable
                            rs.getInt("IsDeleted"),
                            rs.getString("Status"),
                            rs.getDouble("Balance"),
                            rs.getTimestamp("CreatedAt"),
                            rs.getTimestamp("UpdatedAt") // nullable
                    );
                    list.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateProfileCustomer(String email, String address, String phoneNumber, String password) {
        String query = "UPDATE Account SET Address = ?, PhoneNumber = ?, Password = ?, UpdatedAt = ? WHERE Email = ?";
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now()); // Lấy thời gian hiện tại

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, address);         // Cập nhật địa chỉ
            ps.setString(2, phoneNumber);     // Cập nhật số điện thoại
            ps.setString(3, password);        // Cập nhật mật khẩu
            ps.setTimestamp(4, currentTimestamp); // Cập nhật thời gian sửa đổi
            ps.setString(5, email);           // Xác định tài khoản cần cập nhật bằng email
            return ps.executeUpdate() > 0;    // Trả về true nếu cập nhật thành công
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public boolean updateProfileStaffs(String email, String address, String phoneNumber, String password) {
        String query = "UPDATE Account SET Address = ?, PhoneNumber = ?, Password = ?, UpdatedAt = ? WHERE Email = ?";
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now()); // Lấy thời gian hiện tại

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, address);         // Cập nhật địa chỉ
            ps.setString(2, phoneNumber);     // Cập nhật số điện thoại
            ps.setString(3, password);        // Cập nhật mật khẩu
            ps.setTimestamp(4, currentTimestamp); // Cập nhật thời gian sửa đổi
            ps.setString(5, email);           // Xác định tài khoản cần cập nhật bằng email
            return ps.executeUpdate() > 0;    // Trả về true nếu cập nhật thành công
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public boolean isPhoneNumberExists(String phoneNumber) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Account WHERE PhoneNumber = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean insertNewStaff(String username, String email, String password, String phoneNumber, int roleID) {
        String sql = "INSERT INTO Account (RoleID, AccountName, Email, Password, PhoneNumber, Image) VALUES (?, ?, ?, ?, ?, ?)";
        String defaultImageUrl = "./assets/img/default-img-profile.png";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleID); // Sử dụng roleID từ tham số
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, phoneNumber);
            stmt.setString(6, defaultImageUrl);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Lỗi khi insert staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void insertNewAccount(String username, String email, String password, String phoneNumber) {
        String sql = "INSERT INTO Account (RoleID, AccountName, Email, Password, PhoneNumber, Image) VALUES (?, ?, ?, ?, ?, ?)";
        String defaultImageUrl = "./assets/img/default-img-profile.png";
        int defaultRoleID = 3;

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, defaultRoleID);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, phoneNumber);
            stmt.setString(6, defaultImageUrl);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public void insertNewAccount(String username, String email, String password, String phoneNumber) {
//        String sql = "INSERT INTO Account (RoleID, AccountName, Email, Password, PhoneNumber, Image) VALUES (?, ?, ?, ?, ?, ?)";
//        String defaultImageUrl = "./assets/img/default-img-profile.png";
//        int defaultRoleID = 3;
//
//        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, defaultRoleID);
//            stmt.setString(2, username);
//            stmt.setString(3, email);
//            stmt.setString(4, password);
//            stmt.setString(5, phoneNumber);
//            stmt.setString(6, defaultImageUrl);
//            stmt.executeUpdate();
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
    public Account authenticateUser(String email, String password) {
        String hashedPassword = MyUtils.hashPassword(password); // Hash mật khẩu người dùng nhập
        String query = "SELECT * FROM Account WHERE Email = ? AND Password = ? AND IsDeleted = 0";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword); // So sánh với chuỗi hash trong DB

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getInt("AccountID"));
                    account.setRoleID(rs.getInt("RoleID"));
                    account.setUserName(rs.getString("AccountName"));
                    account.setEmail(rs.getString("Email"));
                    account.setPhoneNumber(rs.getString("PhoneNumber"));
                    account.setStatus(rs.getString("Status"));
                    account.setBalance(rs.getDouble("Balance"));
                    account.setImage(rs.getString("Image"));
                    account.setIsDeleted(rs.getInt("IsDeleted"));
                    account.setAddress(rs.getString("Address"));
                    account.setPassword(rs.getString("Password"));
                    return account;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account authenticateAdminStaff(String email, String password) {
        String hashedPassword = MyUtils.hashPassword(password); // Hash mật khẩu người dùng nhập
        String query = "SELECT * FROM Account WHERE Email = ? AND Password = ? AND IsDeleted = 0";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword); // So sánh với chuỗi hash trong DB

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getInt("AccountID"));
                    account.setRoleID(rs.getInt("RoleID"));
                    account.setUserName(rs.getString("AccountName"));
                    account.setEmail(rs.getString("Email"));
                    account.setPhoneNumber(rs.getString("PhoneNumber"));
                    account.setStatus(rs.getString("Status"));
                    account.setBalance(rs.getDouble("Balance"));
                    account.setImage(rs.getString("Image"));
                    account.setAddress(rs.getString("Address"));
                    account.setPassword(rs.getString("Password"));
                    return account;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // cua customer
    public String validateResetToken(String token) {
        String query = "SELECT Email FROM Account WHERE ResetToken = ? AND TokenExpiry > CURRENT_TIMESTAMP";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Email");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePassword(String email, String newPassword) {
        String hashedPassword = MyUtils.hashPassword(newPassword);
        String query = "UPDATE Account SET Password = ?, ResetToken = NULL, TokenExpiry = NULL WHERE Email = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // cua admin va staff 
    public String validateResetTokenAdmin(String token) {
        String query = "SELECT Email FROM Account WHERE ResetToken = ? AND TokenExpiry > CURRENT_TIMESTAMP";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Email");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePasswordAdmin(String email, String newPassword) {
        String hashedPassword = MyUtils.hashPassword(newPassword);
        String query = "UPDATE Account SET Password = ?, ResetToken = NULL, TokenExpiry = NULL WHERE Email = ?";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
