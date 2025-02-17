/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.AccountDAO.conn;
import static DAO.AccountDAO.stm;
import DBConnect.DBConnection;
import Model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class MyUntilsDAO {

    protected static String TOTAL_PAGE = "SELECT COUNT(*) FROM Account Where RoleID = ? ";
    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    public int getTotalPages(int pageSize, int RoleID) throws ClassNotFoundException {
        int totalRecords = 0;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(TOTAL_PAGE);
            stm.setInt(1, RoleID);
            rs = stm.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (int) Math.ceil((double) totalRecords / pageSize);
    }

}
