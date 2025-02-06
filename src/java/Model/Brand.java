/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author admin1
 */
public class Brand {

    private int brandID;
    private String brandName;
    private String originBrand;  // Cột mới
    private Date createdAt;

    public Brand() {
    }

    public Brand(int brandID, String brandName, String originBrand, Date createdAt) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.originBrand = originBrand;  // Thêm originBrand vào constructor
        this.createdAt = createdAt;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOriginBrand() {
        return originBrand;  // Getter cho cột OriginBrand
    }

    public void setOriginBrand(String originBrand) {
        this.originBrand = originBrand;  // Setter cho cột OriginBrand
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
