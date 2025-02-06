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
public class Age {

    private int ageID;
    private String ageRange;
    private Date createdAt;

    public Age() {
    }

    public Age(int ageID, String ageRange, Date createdAt) {
        this.ageID = ageID;
        this.ageRange = ageRange;
        this.createdAt = createdAt;
    }

    public int getAgeID() {
        return ageID;
    }

    public void setAgeID(int ageID) {
        this.ageID = ageID;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
