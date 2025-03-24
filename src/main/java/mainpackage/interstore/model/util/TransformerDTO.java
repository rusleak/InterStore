package mainpackage.interstore.model.util;

import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.MainCategory;

import java.util.ArrayList;
import java.util.List;

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
}
