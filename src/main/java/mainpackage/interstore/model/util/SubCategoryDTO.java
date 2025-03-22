package mainpackage.interstore.model.util;

import jakarta.persistence.Column;
import mainpackage.interstore.model.MainCategory;

public class SubCategoryDTO {
    private Long mainCategoryId;

    private String name;
    private String newName;

    public Long getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(Long mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
