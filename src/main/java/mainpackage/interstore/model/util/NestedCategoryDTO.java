package mainpackage.interstore.model.util;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import mainpackage.interstore.model.Subcategory;
import org.springframework.lang.Nullable;

public class NestedCategoryDTO {
    private Long subcategoryId;
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must start from capital letter")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must start from capital letter")
    private String newName;

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
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
