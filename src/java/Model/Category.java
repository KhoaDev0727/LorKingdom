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
public class Category {
    private int categoryID;
    private int superCategoryID;
    private String name;
    private Date createdAt;

    public Category() {
    }

    public Category(int categoryID, int superCategoryID, String name, Date createdAt) {
        this.categoryID = categoryID;
        this.superCategoryID = superCategoryID;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getSuperCategoryID() {
        return superCategoryID;
    }

    public void setSuperCategoryID(int superCategoryID) {
        this.superCategoryID = superCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
