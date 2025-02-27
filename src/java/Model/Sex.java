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
public class Sex {

    private int sexID;
    private String name;
    private int isDeleted; // Thêm thuộc tính này để tương ứng với cột IsDeleted trong bảng
    private Date createdAt;

    public Sex(int sexID, String name, int isDeleted, Date createdAt) {
        this.sexID = sexID;
        this.name = name;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public int getSexID() {
        return sexID;
    }

    public void setSexID(int sexID) {
        this.sexID = sexID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

   
}
