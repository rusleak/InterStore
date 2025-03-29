package mainpackage.interstore.model.DTOs;

import mainpackage.interstore.model.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransformerDTO {

    public static MainCategoryDTO mainCategoryToDto(MainCategory mainCategory) {
        MainCategoryDTO mainCategoryDTO = new MainCategoryDTO();
        mainCategoryDTO.setId(mainCategory.getId());
        mainCategoryDTO.setName(mainCategory.getName());
        mainCategoryDTO.setImageUrl(mainCategory.getImageUrl());
        return mainCategoryDTO;
    }

    public static List<MainCategoryDTO> listOfMainCatToDTO(List<MainCategory> mainCategoryList) {
        List<MainCategoryDTO> mainCategoryDTOList = new ArrayList<>();
        for (MainCategory mainCategory : mainCategoryList) {
            mainCategoryDTOList.add(mainCategoryToDto(mainCategory));
        }
        return mainCategoryDTOList;
    }

    public static MainCategory dtoToMainCategory(MainCategoryDTO mainCategoryDTO) {
        MainCategory mainCategory = new MainCategory();

        mainCategory.setName(mainCategoryDTO.getName());
        if (mainCategoryDTO.getImageUrl() != null) {
            mainCategory.setImageUrl(mainCategoryDTO.getImageUrl());
        }
        return mainCategory;
    }

    public static Subcategory dtoToSubCategory(SubCategoryDTO subCategoryDTO, MainCategory mainCategory) {
        Subcategory subcategory = new Subcategory();
        subcategory.setName(subCategoryDTO.getName());
        subcategory.setMainCategory(mainCategory);
        return subcategory;
    }

    public static SubCategoryDTO subCategoryToDto(Subcategory subcategory) {
        SubCategoryDTO subcategoryDTO = new SubCategoryDTO();
        subcategoryDTO.setId(subcategory.getId());
        subcategoryDTO.setName(subcategory.getName());
        subcategoryDTO.setMainCategoryId(subcategory.getMainCategory().getId());
        return subcategoryDTO;
    }

    public static List<SubCategoryDTO> listOfSubCatToDTO(List<Subcategory> subcategoryList) {
        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        for (Subcategory subcategory : subcategoryList) {
            subCategoryDTOS.add(subCategoryToDto(subcategory));
        }
        return subCategoryDTOS;
    }

    public static NestedCategory dtoToNestedCategory(NestedCategoryDTO nestedCategoryDTO, Subcategory subcategory) {
        NestedCategory nestedCategory = new NestedCategory();
        nestedCategory.setName(nestedCategoryDTO.getName());
        nestedCategory.setSubcategory(subcategory);
        return nestedCategory;
    }

    public static NestedCategoryDTO nestedCategoryToDTO(NestedCategory nestedCategory) {
        NestedCategoryDTO nestedCategoryDTO = new NestedCategoryDTO();
        nestedCategoryDTO.setId(nestedCategory.getId());
        nestedCategoryDTO.setName(nestedCategory.getName());
        nestedCategoryDTO.setSubCategoryId(nestedCategory.getSubcategory().getId());
        return nestedCategoryDTO;
    }

    public static List<NestedCategoryDTO> listOfNestedCatToDTO(List<NestedCategory> nestedCategoryList) {
        List<NestedCategoryDTO> nestedCategories = new ArrayList<>();
        for (NestedCategory nestedCategory : nestedCategoryList) {
            nestedCategories.add(nestedCategoryToDTO(nestedCategory));
        }
        return nestedCategories;
    }

    public static ProductDTO productToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscountedPrice(product.getDiscountedPrice());
        productDTO.setProductImages(product.getProductImages());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setBrandId(product.getBrand().getId());
        productDTO.setOneCId(product.getOneCId());
        productDTO.setIsActive(product.getIsActive());
        productDTO.setIsActive(product.getIsActive());
        // Преобразуем список объектов Color в список строк с их именами
        if (product.getColors() != null) {
            productDTO.setColorIds(
                    product.getColors().stream()
                            .map(Color::getId)
                            .collect(Collectors.toList())
            );
        }

        // Преобразуем список объектов Tag в список строк с их именами
        if (product.getTagList() != null) {
            productDTO.setTagIds(
                    product.getTagList().stream()
                            .map(Tag::getId)
                            .collect(Collectors.toList())
            );
        }
        if (product.getDimensions() != null) {
            productDTO.setDimensionsIds(
                    product.getDimensions().stream()
                            .map(Dimensions::getId)
                            .collect(Collectors.toList())
            );
        }

        return productDTO;
    }
    public static void dtoToProductWithoutRelations(ProductDTO productDTO, Product product) {
//        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setDiscountedPrice(productDTO.getDiscountedPrice());
        product.setOneCId(productDTO.getOneCId());
        product.setIsActive(productDTO.getIsActive());
    }

    public static List<ProductDTO> listOfProductToDTO(List<Product> productList) {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            productDTOList.add(productToDTO(product));
        }
        return productDTOList;
    }

    public static void dtoToBrand(BrandDTO brandDTO, Brand brand) {
        brand.setName(brandDTO.getName());
    }

    public static List<BrandDTO> listOfBrandToDTO(List<Brand> brandList) {
        List<BrandDTO> result = new ArrayList<>();
        for (Brand brand : brandList) {
            result.add(TransformerDTO.brandToDTO(brand));
        }
        return result;
    }

    public static BrandDTO brandToDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId());
        brandDTO.setName(brand.getName());
        return brandDTO;
    }

    public static void dtoToTag(TagDTO tagDTO, Tag tag) {
        tag.setName(tagDTO.getName());
    }

    public static List<TagDTO> listOfTagToDTO(List<Tag> tagList) {
        List<TagDTO> result = new ArrayList<>();
        for (Tag tag : tagList) {
            result.add(TransformerDTO.tagToDTO(tag));
        }
        return result;
    }

    public static TagDTO tagToDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    public static Color dtoToColor(ColorDTO colorDTO, Color color) {
        color.setName(colorDTO.getName());
        return color;
    }

    public static List<ColorDTO> listOfColorsToDTO(List<Color> colorList) {
        List<ColorDTO> result = new ArrayList<>();
        for (Color color : colorList) {
            result.add(TransformerDTO.colorToDTO(color));
        }
        return result;
    }

    public static ColorDTO colorToDTO(Color color) {
        ColorDTO colorDTO = new ColorDTO();
        colorDTO.setId(color.getId());
        colorDTO.setName(color.getName());
        colorDTO.setImageUrl(color.getImageUrl());
        return colorDTO;
    }
}

