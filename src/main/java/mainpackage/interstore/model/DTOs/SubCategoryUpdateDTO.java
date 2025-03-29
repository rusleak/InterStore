package mainpackage.interstore.model.DTOs;

public class SubCategoryUpdateDTO {
    String newName;
    Long newMainCategoryId;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public Long getNewMainCategoryId() {
        return newMainCategoryId;
    }

    public void setNewMainCategoryId(Long newMainCategoryId) {
        this.newMainCategoryId = newMainCategoryId;
    }
}
