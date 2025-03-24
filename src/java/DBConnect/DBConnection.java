package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=LorKingDom;encrypt=false;";
    private static String user = "sa";
    private static String pass = "12345";
    
//    private static String url = "jdbc:sqlserver://QUIN;databaseName=LorKingDomMain;encrypt=true;trustServerCertificate=true";
//    private static String user = "sa";
//    private static String pass = "12345";
//    
//    private static String url = "jdbc:sqlserver://QUIN;databaseName=LorKingDom;encrypt=true;trustServerCertificate=true";
//    private static String user = "sa";
//    private static String pass = "12345";
//    
//    private static String url = "jdbc:sqlserver://QUIN;databaseName=LorKingDom;encrypt=true;trustServerCertificate=true";
//    private static String user = "sa";
//    private static String pass = "12345";
//  
  
//    private static String url = "jdbc:sqlserver://LEMINHKHOA:1433;databaseName=LorKingDom;encrypt=true;trustServerCertificate=true";
//    private static String user = "sa";
//    private static String pass = "12345";   

//    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=LorKingDom;encrypt=true;trustServerCertificate=true";
//    private static String user = "sa";
//    private static String pass = "khangmc1502@";

//    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=LorKingDom2;encrypt=true;trustServerCertificate=true";
//    private static String user = "sa";
//    private static String pass = "123456789";
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Kết nối thành công!");
        } else {
            System.out.println("Kết nối thất bại!");
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
