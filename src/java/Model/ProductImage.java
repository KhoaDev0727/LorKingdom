/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class ProductImage {

    private int imageID;
    private String productID;
    private String imageUrl;
    private int isMain;

    public ProductImage(int imageID, String productID, String imageUrl, int isMain) {
        this.imageID = imageID;
        this.productID = productID;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

}
