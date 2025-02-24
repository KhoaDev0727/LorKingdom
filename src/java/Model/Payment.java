package Model;

public class Payment {

    private int paymentMethodID;
    private String methodName;
    private String description;

    public Payment() {
    }

    public Payment(int paymentMethodID, String methodName, String description) {
        this.paymentMethodID = paymentMethodID;
        this.methodName = methodName;
        this.description = description;
    }

    public int getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(int paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
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
