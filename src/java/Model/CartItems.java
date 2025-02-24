/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class CartItems {

    private Product product;
    private int AccountID;
    private double price;
    private int Quantity;

    public CartItems(Product product, int AccountID, int Quantity) {
        this.product = product;
        this.AccountID = AccountID;
        this.Quantity = Quantity;
    }

    public CartItems(Product product, double price, int Quantity) {
        this.product = product;
        this.price = price;
        this.Quantity = Quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int AccountID) {
        this.AccountID = AccountID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

}
