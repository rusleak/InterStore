package mainpackage.interstore.model.DTOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import mainpackage.interstore.model.Product;

import java.util.List;

public class BrandDTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
