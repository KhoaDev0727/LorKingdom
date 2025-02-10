/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.PriceRange;
import DBConnect.DBConnection;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class PriceRangeDAO {

    protected static PreparedStatement stm = null;
    protected static ResultSet rs = null;
    protected static Connection conn = null;

    private static String SELECT_PRICERANGE = "SELECT * FROM PriceRange";
    private static String ADD_PRICERANGE = "INSERT INTO PriceRange(PriceFrom, PriceTo) VALUES (?, ?)";
    private static String UPDATE_PRICERANGE_BY_ID = "UPDATE PriceRange SET PriceFrom = ?, PriceTo = ? WHERE PriceRangeID = ?";
    private static String DELETE_PRICERANGE_BY_ID = "DELETE FROM PriceRange WHERE PriceRangeID = ?;";

    public static List<PriceRange> showPriceRange() {
        List<PriceRange> list = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(SELECT_PRICERANGE);
            rs = stm.executeQuery();
            while (rs.next()) {
                PriceRange p = new PriceRange(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3));
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public static boolean addPriceRange(int priceFrom, int priceTo) {
        boolean rowUpdate = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(ADD_PRICERANGE);
            stm.setInt(1, priceFrom);
            stm.setInt(2, priceTo);
            rowUpdate = stm.executeUpdate() > 0;
            return rowUpdate;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowUpdate;
    }

    public static boolean UpdatePriceRangeById(PriceRange priceRange) {
        boolean rowUpdate = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(UPDATE_PRICERANGE_BY_ID);
            stm.setInt(1, priceRange.getPriceFrom());
            stm.setInt(2, priceRange.getPriceTo());
            stm.setInt(3, priceRange.getPriceRangeID());
            rowUpdate = stm.executeUpdate() > 0;
            return rowUpdate;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowUpdate;
    }
     public static boolean deletePriceRangeById(int priceRangeId) {
        boolean rowUpdate = false;
        try {
            conn = DBConnection.getConnection();
            stm = conn.prepareStatement(DELETE_PRICERANGE_BY_ID);
            stm.setInt(1, priceRangeId);
            rowUpdate = stm.executeUpdate() > 0;
            return rowUpdate;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowUpdate;
    }

}
