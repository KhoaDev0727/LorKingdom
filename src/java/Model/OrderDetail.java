/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;



/**
 *
 * @author Acer
 */
public class OrderDetail {
    private int orderDetailID;
    private int orderID;
    private String productName;
    private int quantity;
    float unitPrice;
    float discount;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailID, int orderID, String productName, int quantity, float unitPrice, float discount) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public int getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "orderDetailID=" + orderDetailID + ", orderID=" + orderID + ", productName=" + productName + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", discount=" + discount + '}';
    }

    
    
    
}
