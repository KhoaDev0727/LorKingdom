/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class CategorySales {

    private String categoryName;
    private int QuantityCartegory;

    public CategorySales() {
    }

    public CategorySales(String categoryName, int QuantityCartegory) {
        this.categoryName = categoryName;
        this.QuantityCartegory = QuantityCartegory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getQuantityCartegory() {
        return QuantityCartegory;
    }

    public void setQuantityCartegory(int QuantityCartegory) {
        this.QuantityCartegory = QuantityCartegory;
    }

}
