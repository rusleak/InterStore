package mainpackage.interstore.model.DTOs;

import java.util.List;

public class ProductFilterDTO {
    private Long subcategoryId;
    private Long nestedCategoryId;

    private String filterMinPrice;
    private String filterMaxPrice;

    public List<Long> colors;
    private List<String> dimensions;
    private List<String> tags;
    private List<String> brands;

    private int page;
    private int size = 16;

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Long getNestedCategoryId() {
        return nestedCategoryId;
    }

    public void setNestedCategoryId(Long nestedCategoryId) {
        this.nestedCategoryId = nestedCategoryId;
    }

    public String getFilterMinPrice() {
        return filterMinPrice;
    }

    public void setFilterMinPrice(String filterMinPrice) {
        this.filterMinPrice = filterMinPrice;
    }

    public String getFilterMaxPrice() {
        return filterMaxPrice;
    }

    public void setFilterMaxPrice(String filterMaxPrice) {
        this.filterMaxPrice = filterMaxPrice;
    }

    public List<Long> getColors() {
        return colors;
    }

    public void setColors(List<Long> colors) {
        this.colors = colors;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}