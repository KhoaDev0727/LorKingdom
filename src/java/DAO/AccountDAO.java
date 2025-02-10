/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

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
    private static String SELECT_BY_ROLE_CUSTOMER = "SELECT * FROM Account WHERE RoleID = ?";
    private static String UPDATE_PROFILE = "UPDATE Account SET AccountName = ?, PhoneNumber = ?, Em\"UPDATE Account SET AccountName = ?, PhoneNumber = ?, Email ail = ?,Image = ?, Password = ?, Address = ?, Status = ?, Balance = ?, UpdatedAt = ? WHERE AccountID = ?";
    private static String UPDATE_PROFILE_STAFF = "UPDATE Account SET AccountName = ?, PhoneNumber = ?, Email = ?, Password = ?, Address = ?, Status = ?,Image = ?, UpdatedAt = ? WHERE AccountID = ?";
    private static String DELETE_ACCOUNT_SOFT = "UPDATE Account SET isDeleted = ?, Status= ? WHERE AccountID = ?";
    private static String DELETE_ACCOUNT_BY_ID = "DELETE FROM Account WHERE AccountID = ?";
    private static String SEARCH_ACCOUNT = "SELECT * FROM Account WHERE ( AccountID = ? OR AccountName LIKE ? OR PhoneNumber LIKE ? OR email LIKE ? ) AND RoleID = ?";
    private static String ADD_ADMIN = "INSERT INTO Account (AccountName, PhoneNumber, Email, Image, Password, Address, Status, RoleID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

    public static List<Account> showList(int roleId) {
        List<Account> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_BY_ROLE_CUSTOMER);
            stm.setInt(1, roleId);
            rs = stm.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(10));
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

    public static List<Account> searchUser(String keyword, int role) {
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
            stm.setInt(5, role);
            System.out.println(role);
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

}
