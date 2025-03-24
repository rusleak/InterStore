package mainpackage.interstore.model.DTOs;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import mainpackage.interstore.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class MainCategoryDTO {

    private Long id;
//    @Pattern(regexp = "[A-Z][a-z]+", message = "Must start from capital letter")
    private String name;

//    @NotBlank
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
