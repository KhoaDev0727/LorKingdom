/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private double totalAmount;
    private Date createdAt;
    private Date updatedAt;
    private List<OrderDetail> orderDetails = new ArrayList<>();

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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", accountName=" + accountName + ", payMentMethodName=" + payMentMethodName + ", shipingMethodName=" + shipingMethodName + ", orderDate=" + orderDate + ", status=" + status + ", totalAmount=" + totalAmount + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }

}
