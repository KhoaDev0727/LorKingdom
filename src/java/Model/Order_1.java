package Model;

import java.math.BigDecimal;
import java.util.Date;

public class Order_1 {

    private int orderId;
    private String accountName;  // Tên khách hàng thay vì AccountID
    private String payMentMethodName;
    private String shippingMethodName;
    private Date orderDate;
    private String status;
    private double totalAmount;
    private Date createdAt;
    private Date updatedAt;

    public Order_1() {
    }

    public Order_1(int orderId, String accountName,
            String payMentMethodName, String shippingMethodName,
            Date orderDate, String status, double totalAmount,
            Date createdAt, Date updatedAt) {
        this.orderId = orderId;
        this.accountName = accountName;
        this.payMentMethodName = payMentMethodName;
        this.shippingMethodName = shippingMethodName;
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

    public String getShippingMethodName() {
        return shippingMethodName;
    }

    public void setShippingMethodName(String shippingMethodName) {
        this.shippingMethodName = shippingMethodName;
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

    @Override
    public String toString() {
        return String.format("Order{id=%d, accountName='%s', paymentMethod='%s', shippingMethod='%s', orderDate='%s', status='%s', totalAmount=%.2f, createdAt='%s', updatedAt='%s'}",
                orderId, accountName, payMentMethodName, shippingMethodName, orderDate, status, totalAmount, createdAt, updatedAt);
    }
}
