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

    private int BrandID;
    private String brandName;
    private String OriginBrand;  // Cột mới
    private Date CreatedAt;

    public Brand() {
    }

    public Brand(int BrandID, String brandName, String OriginBrand, Date CreatedAt) {
        this.BrandID = BrandID;
        this.brandName = brandName;
        this.OriginBrand = OriginBrand;
        this.CreatedAt = CreatedAt;
    }

    public int getBrandID() {
        return BrandID;
    }

    public void setBrandID(int BrandID) {
        this.BrandID = BrandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOriginBrand() {
        return OriginBrand;
    }

    public void setOriginBrand(String OriginBrand) {
        this.OriginBrand = OriginBrand;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

  
    
}
