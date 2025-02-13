/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author admin1
 */
public class PriceRange {

    private int priceRangeID;     // ID của PriceRange
    private String priceRange;    // Khoảng giá
    private Timestamp createdAt;  // Ngày tạo

    public PriceRange() {
    }

    public PriceRange(int priceRangeID, String priceRange, Timestamp createdAt) {
        this.priceRangeID = priceRangeID;
        this.priceRange = priceRange;
        this.createdAt = createdAt;
    }

    public int getPriceRangeID() {
        return priceRangeID;
    }

    public void setPriceRangeID(int priceRangeID) {
        this.priceRangeID = priceRangeID;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PriceRange{"
                + "priceRangeID=" + priceRangeID
                + ", priceRange='" + priceRange + '\''
                + ", createdAt=" + createdAt
                + '}';
    }
}
