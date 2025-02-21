package Model;

public class Shipping {

    private int shippingMethodID;
    private String methodName;
    private String description;

    public Shipping() {
    }

    public Shipping(int shippingMethodID, String methodName, String description) {
        this.shippingMethodID = shippingMethodID;
        this.methodName = methodName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
