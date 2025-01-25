package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlserver://LEMINHKHOA:1433;databaseName=LorKingDom;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "12345";

    // Phương thức tạo kết nối
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load JDBC Driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        // Tạo kết nối
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
