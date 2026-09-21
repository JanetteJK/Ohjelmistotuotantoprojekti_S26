package entity;

public class StudyMaterial {
    private String material;
    private String category;
    private int userId;

    public StudyMaterial(String material, String category, int userId) {
        this.material = material;
        this.category = category;
        this.userId = userId;
    }

    public String getMaterial() {
        return material;
    }
    public void setMaterials(String material) {
        this.material = material;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public int getUserId() {
        return userId;
    }
}
