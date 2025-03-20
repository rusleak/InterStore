package mainpackage.interstore.model.util;

import lombok.Getter;
import lombok.Setter;
import mainpackage.interstore.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ProductReceiverDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private List<String> productImages = new ArrayList<>();
    private Integer stockQuantity;
    private String brand;
    private List<String> dimensions;
    private String nestedCategory;
    private List<String> colors;
    private List<String> tagList;
    private Long oneC_id;

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

    public Long getOneC_id() {
        return oneC_id;
    }

    public void setOneC_id(Long oneC_id) {
        this.oneC_id = oneC_id;
    }
}
