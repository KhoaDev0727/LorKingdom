/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

/**
 *
 * @author Acer
 */
public class Order {

    private int orderId;
    private String accountName;  // Tên khách hàng thay vì AccountID
    private String payMentMethodName;
    private String shipingMethodName;
    private Date orderDate;
    private String status;
    private float totalAmount;
    private Date createdAt;
    private Date updatedAt;

    public Order() {
    }

    public Order(int orderId, String accountName, String payMentMethodName, String shipingMethodName, Date orderDate, String status, float totalAmount, Date createdAt, Date updatedAt) {
        this.orderId = orderId;
        this.accountName = accountName;
        this.payMentMethodName = payMentMethodName;
        this.shipingMethodName = shipingMethodName;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPayMentMethodName() {
        return payMentMethodName;
    }

    public void setPayMentMethodName(String payMentMethodName) {
        this.payMentMethodName = payMentMethodName;
    }

    public String getShipingMethodName() {
        return shipingMethodName;
    }

    public void setShipingMethodName(String shipingMethodName) {
        this.shipingMethodName = shipingMethodName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", accountName=" + accountName + ", payMentMethodName=" + payMentMethodName + ", shipingMethodName=" + shipingMethodName + ", orderDate=" + orderDate + ", status=" + status + ", totalAmount=" + totalAmount + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }

    
    

    

}
