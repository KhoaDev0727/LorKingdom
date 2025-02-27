/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

public class Origin {

    private int originID;
    private String name;
    private Date createdAt;
    private int isDeleted;

    public Origin() {
        this.isDeleted = 0;
    }

    public Origin(int originID, String name, Date createdAt, int isDeleted) {
        this.originID = originID;
        this.name = name;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public int getOriginID() {
        return originID;
    }

    public void setOriginID(int originID) {
        this.originID = originID;
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
