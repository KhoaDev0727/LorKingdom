/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

public class Notification {

    private int notificationID;
    private String title;
    private String content;
    private String type;
    private String status;
    private Integer accountID; // Nullable
    private boolean isDeleted;
    private Timestamp createdAt;
    private String relativeTime;

    public Notification() {
        this.isDeleted = false;
        this.status = "Unread"; // Default per schema
    }

    public Notification(int notificationID, String title, String content, String type, String status, Integer accountID, boolean isDeleted, Timestamp createdAt) {
        this.notificationID = notificationID;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.accountID = accountID;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return "Read".equalsIgnoreCase(this.status);
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }
}
