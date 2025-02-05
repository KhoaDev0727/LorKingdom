package Model;

/**
 * Class đại diện cho vật liệu
 */
public class Material {

    private int materialID;         // ID của vật liệu
    private String materialName;    // Tên của vật liệu
    private String description;     // Mô tả của vật liệu

    // Constructor mặc định
    public Material() {
    }

    // Constructor có tham số
    public Material(int materialID, String materialName, String description) {
        this.materialID = materialID;
        this.materialName = materialName;
        this.description = description;
    }

    // Getter và Setter cho materialID
    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    // Getter và Setter cho materialName
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    // Getter và Setter cho description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Phương thức toString để hiển thị thông tin của Material
    @Override
    public String toString() {
        return "Material{" +
                "materialID=" + materialID +
                ", materialName='" + materialName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
