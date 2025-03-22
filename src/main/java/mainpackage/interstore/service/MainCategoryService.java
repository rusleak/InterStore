package mainpackage.interstore.service;

import jakarta.persistence.EntityExistsException;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.util.MainCategoryDTO;
import mainpackage.interstore.model.util.MultipartProcesses;
import mainpackage.interstore.repository.MainCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class MainCategoryService {

    private final SubcategoryService subcategoryService;
    private final MainCategoryRepository mainCategoryRepository;
    private final NestedCategoryService nestedCategoryService;
    @Autowired
    public MainCategoryService(SubcategoryService subcategoryService, MainCategoryRepository mainCategoryRepository, NestedCategoryService nestedCategoryService) {
        this.subcategoryService = subcategoryService;
        this.mainCategoryRepository = mainCategoryRepository;
        this.nestedCategoryService = nestedCategoryService;
    }

    public List<MainCategory> findAll() {
        return mainCategoryRepository.findAll();
    }


    public Optional<MainCategory> findById(long id) {
        return mainCategoryRepository.findById(id);
    }
    public String getActiveCategory(Long mainCategoryId, Long subCategoryId, Long nestedCategoryId, List<Product> products) {
        String currentActiveCategory = "";
        if(nestedCategoryId != null) {
            Optional<NestedCategory> nestedCategory = nestedCategoryService.findById(nestedCategoryId);
            String subcategoryName = nestedCategory.get().getSubcategory().getName();
            String nestedCategoryName = nestedCategory.get().getName();
            currentActiveCategory = subcategoryName + " -> " + nestedCategoryName;
        } else if (subCategoryId != null && nestedCategoryId == null) {
            currentActiveCategory = subcategoryService.findById(subCategoryId).get().getName();
        } else {
            currentActiveCategory = mainCategoryRepository.findById(mainCategoryId).get().getName();
        }

        if(products == null || products.isEmpty()) {
            currentActiveCategory = currentActiveCategory + "<br>На разі немає товарів з такими фільтрами або з цієї категорії";
        }

        return currentActiveCategory;
    }

    public void receiveMainCategory(MainCategoryDTO mainCategoryDTO, MultipartFile multipartFile, String pathToSavePict) throws IOException {
        Optional<MainCategory> mainCategory = mainCategoryRepository.findByName(mainCategoryDTO.getName());
        Optional<MainCategory> mainCategoryNewName = mainCategoryRepository.findByName(mainCategoryDTO.getNewName());
        if(mainCategoryNewName.isPresent()) {
            throw new EntityExistsException("Main category with provided new name already exists");
        }
        MainCategory foundMainCategory;
        if(mainCategory.isPresent()){
            foundMainCategory = mainCategory.get();
        } else {
            foundMainCategory = new MainCategory();
        }
        MultipartProcesses.processMultipartFile(multipartFile,pathToSavePict);
        if(mainCategoryDTO.getNewName() != null) {
            foundMainCategory.setName(mainCategoryDTO.getNewName());
        } else {
            foundMainCategory.setName(mainCategoryDTO.getName());
        }
        foundMainCategory.setImageUrl(mainCategoryDTO.getImageUrl());
        mainCategoryRepository.save(foundMainCategory);
    }

}

