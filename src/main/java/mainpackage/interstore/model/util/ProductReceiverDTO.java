package mainpackage.interstore.model.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mainpackage.interstore.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductReceiverDTO {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    private BigDecimal discountedPrice;

    @NotNull(message = "Product images list cannot be null")
    private List<String> productImages = new ArrayList<>();

    @NotNull(message = "Stock quantity cannot be null")
    private Integer stockQuantity;

    private String brand;

    @NotNull(message = "Dimensions list cannot be null")
    @NotEmpty(message = "Dimensions list cannot be empty")
    private List<String> dimensions;

    @NotBlank(message = "Nested category cannot be blank")
    private String nestedCategory;

    @NotNull(message = "Colors list cannot be null")
    @NotEmpty(message = "Colors list cannot be empty")
    private List<String> colors;

    @NotNull(message = "Tag list cannot be null")
    @NotEmpty(message = "Tag list cannot be empty")
    private List<String> tagList;

    @NotNull(message = "1C ID cannot be null")
    private Long oneCId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<String> productImages) {
        this.productImages = productImages;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public String getNestedCategory() {
        return nestedCategory;
    }

    public void setNestedCategory(String nestedCategory) {
        this.nestedCategory = nestedCategory;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public Long getOneCId() {
        return oneCId;
    }

    public void setOneCId(Long oneCId) {
        this.oneCId = oneCId;
    }

}
