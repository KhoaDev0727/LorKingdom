/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

/**
 *
 * @author admin1
 */
public class SuperCategory {

    private int superCategoryID;
    private String name;
    private Date createdAt;
    private int isDeleted;

    public SuperCategory() {
        this.isDeleted = 0;
    }

    public SuperCategory(int superCategoryID, String name, Date createdAt, int isDeleted) {
        this.superCategoryID = superCategoryID;
        this.name = name;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
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

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
