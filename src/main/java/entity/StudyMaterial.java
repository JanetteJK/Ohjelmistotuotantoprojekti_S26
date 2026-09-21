package entity;

public class StudyMaterial {
    private String materials;
    private String category;
    private int userId;

    public StudyMaterial(String materials, String category, int userId) {
        this.materials = materials;
        this.category = category;
        this.userId = userId;
    }

    public String getMaterials() {
        return materials;
    }
    public void setMaterials(String materials) {
        this.materials = materials;
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
