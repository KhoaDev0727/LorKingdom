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
public class Sex {

    private int sexID;          // ID của giới tính
    private String name;        // Tên giới tính (Male, Female, Unisex, ...)
    private Timestamp createdAt; // Ngày tạo

    public Sex() {
    }

    public Sex(int sexID, String name, Timestamp createdAt) {
        this.sexID = sexID;
        this.name = name;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Sex{"
                + "sexID=" + sexID
                + ", name='" + name + '\''
                + ", createdAt=" + createdAt
                + '}';
    }
}
