package mainpackage.interstore.model.util;

import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Subcategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
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
        if(mainCategoryDTOList.isEmpty()) {
            throw new EntityNotFoundException("List of MainCategories is empty");
        }
        return mainCategoryDTOList;
    }

    public static MainCategory dtoToMainCategory(MainCategoryDTO mainCategoryDTO) {
        MainCategory mainCategory = new MainCategory();

        mainCategory.setName(mainCategoryDTO.getName());
        if(mainCategoryDTO.getImageUrl() != null) {
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
        if(subCategoryDTOS.isEmpty()) {
            throw new EntityNotFoundException("List of SubCategories is empty");
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
        if(nestedCategories.isEmpty()) {
            throw new EntityNotFoundException("List of Nested Categories is empty");
        }
        return nestedCategories;
    }
}
