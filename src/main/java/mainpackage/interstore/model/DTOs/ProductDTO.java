package mainpackage.interstore.model.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {
    private Long id;
    private String name;
    private Long nestedCategoryId;
    private String description;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private List<String> productImages;
    private Integer stockQuantity;
    private Long brandId;
    private List<Long> colorIds;
    private List<Long> tagIds;
    private List<Long> dimensionsIds;
    private Long oneCId;
    private Integer isActive;

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

    public Long getNestedCategoryId() {
        return nestedCategoryId;
    }

    public void setNestedCategoryId(Long nestedCategoryId) {
        this.nestedCategoryId = nestedCategoryId;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public List<Long> getColorIds() {
        return colorIds;
    }

    public void setColorIds(List<Long> colorIds) {
        this.colorIds = colorIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public List<Long> getDimensionsIds() {
        return dimensionsIds;
    }

    public void setDimensionsIds(List<Long> dimensionsIds) {
        this.dimensionsIds = dimensionsIds;
    }

    public Long getOneCId() {
        return oneCId;
    }

    public void setOneCId(Long oneCId) {
        this.oneCId = oneCId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
