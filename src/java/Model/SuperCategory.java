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

    // Constructor không tham số
    public SuperCategory() {
    }

    // Constructor có tham số
    public SuperCategory(int superCategoryID, String name, Date createdAt) {
        this.superCategoryID = superCategoryID;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Getter và Setter
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
