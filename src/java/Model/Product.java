/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class Product {

    private int productID;
    private String SKU; // -- ID phụ (mã sản phẩm duy nhất)
    private Integer categoryID;
    private Integer materialID;
    private Integer ageID;
    private Integer sexID;
    private Integer priceRangeID;
    private Integer brandID;
    private Integer originID;
    private String name;
    private double price;
    private int quantity;
    private String status;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Product(String SKU, Integer categoryID, Integer materialID, Integer ageID, Integer sexID, Integer priceRangeID, Integer brandID, Integer originID, String name, double price, int quantity, String status, String description) {
        this.SKU = SKU;
        this.categoryID = categoryID;
        this.materialID = materialID;
        this.ageID = ageID;
        this.sexID = sexID;
        this.priceRangeID = priceRangeID;
        this.brandID = brandID;
        this.originID = originID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
    }

    public Product(String productID, Integer categoryID, Integer materialID, Integer ageID, Integer sexID, Integer priceRangeID, Integer brandID, Integer originID, String name, double price, int quantity, String status, String description, Timestamp createdAt, Timestamp updatedAt) {
        this.SKU = SKU;
        this.categoryID = categoryID;
        this.materialID = materialID;
        this.ageID = ageID;
        this.sexID = sexID;
        this.priceRangeID = priceRangeID;
        this.brandID = brandID;
        this.originID = originID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(String SKU, Integer categoryID, Integer ageID, Integer sexID, Integer priceRangeID, Integer brandID, Integer originID, String name, double price, int quantity, String status, String description) {
        this.SKU = SKU;
        this.categoryID = categoryID;
        this.ageID = ageID;
        this.sexID = sexID;
        this.priceRangeID = priceRangeID;
        this.brandID = brandID;
        this.originID = originID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
    }

    public Product(String SKU, Integer category, Integer material, Integer ageGroup, Integer gender, Integer priceRange, Integer brand, Integer origin, String productName, double price, int stockQuantity, String description) {
        this.SKU = SKU;
        this.categoryID = category;
        this.materialID = material;
        this.ageID = ageGroup;
        this.sexID = gender;
        this.priceRangeID = priceRange;
        this.brandID = brand;
        this.originID = origin;
        this.name = productName;
        this.price = price;
        this.quantity = stockQuantity;

        this.description = description;
    }

    public Product() {
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getMaterialID() {
        return materialID;
    }

    public void setMaterialID(Integer materialID) {
        this.materialID = materialID;
    }

    public Integer getAgeID() {
        return ageID;
    }

    public void setAgeID(Integer ageID) {
        this.ageID = ageID;
    }

    public Integer getSexID() {
        return sexID;
    }

    public void setSexID(Integer sexID) {
        this.sexID = sexID;
    }

    public Integer getPriceRangeID() {
        return priceRangeID;
    }

    public void setPriceRangeID(Integer priceRangeID) {
        this.priceRangeID = priceRangeID;
    }

    public Integer getBrandID() {
        return brandID;
    }

    public void setBrandID(Integer brandID) {
        this.brandID = brandID;
    }

    public Integer getOriginID() {
        return originID;
    }

    public void setOriginID(Integer originID) {
        this.originID = originID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}
