package Model;

/**
 * Model cho danh mục sản phẩm
 */
public class Category {

    private int categoryID;
    private String name;
    private String createdAt;

    // Constructor mặc định
    public Category() {
    }

    // Constructor có tham số
    public Category(int categoryID, String name, String createdAt) {
        this.categoryID = categoryID;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
