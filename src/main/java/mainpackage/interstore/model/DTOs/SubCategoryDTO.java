package mainpackage.interstore.model.DTOs;

import jakarta.persistence.Column;
import mainpackage.interstore.model.MainCategory;

public class SubCategoryDTO {
    private Long id;
    private Long mainCategoryId;

    private String name;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
