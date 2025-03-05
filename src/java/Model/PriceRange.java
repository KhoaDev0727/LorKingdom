/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author admin1
 */
public class PriceRange {

    private int priceRangeID;
    private String priceRange;
    private Date createdAt;
    private int isDeleted;

    public PriceRange() {
        this.isDeleted = 0;
    }

    public PriceRange(int priceRangeID, String priceRange, Date createdAt, int isDeleted) {
        this.priceRangeID = priceRangeID;
        this.priceRange = priceRange;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
