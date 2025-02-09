package Model;

import java.math.BigDecimal;

public class Shipping {

    private int shippingMethodID;
    private String methodName;
    private BigDecimal price;
    private String description;

    public Shipping() {
    }

    public Shipping(int shippingMethodID, String methodName, BigDecimal price, String description) {
        this.shippingMethodID = shippingMethodID;
        this.methodName = methodName;
        this.price = price;
        this.description = description;
    }

    public int getShippingMethodID() {
        return shippingMethodID;
    }

    public void setShippingMethodID(int shippingMethodID) {
        this.shippingMethodID = shippingMethodID;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
