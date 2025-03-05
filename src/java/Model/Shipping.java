/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Shipping {

    private int shippingMethodID;
    private String methodName;
    private String description;
    private int isDeleted;

     public Shipping() {
        this.isDeleted = 0;
    }
    
    public Shipping(int shippingMethodID, String methodName, String description, int isDeleted) {
        this.shippingMethodID = shippingMethodID;
        this.methodName = methodName;
        this.description = description;
        this.isDeleted = isDeleted;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
    
}
