package mainpackage.interstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.util.FileManager;
import mainpackage.interstore.model.util.MainCategoryUpdateDTO;
import mainpackage.interstore.repository.MainCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.CoWebFilter;

import javax.management.relation.RelationException;
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


    public Optional<MainCategory> findById(Long id) {
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

    public void delete(Long id) throws RelationException {
        MainCategory mainCategory = mainCategoryIsNotFound(id);
            if (mainCategory.getSubCategories().size() > 0) {
                throw new RelationException("Can't delete main category which have subcategories in it");
            }
            mainCategoryRepository.delete(mainCategory);
    }
    public MainCategory mainCategoryIsNotFound(Long mainCatId) {
        Optional<MainCategory> optionalMainCategory = findById(mainCatId);
        if(optionalMainCategory.isEmpty()) {
            throw new EntityNotFoundException("Main category with id " + mainCatId + "not found");
        }
        return optionalMainCategory.get();
    }
    public boolean mainCategoryExists(String name) {
        Optional<MainCategory> optionalMainCategory = mainCategoryRepository.findByName(name);
        if(optionalMainCategory.isPresent()) {
            throw new EntityExistsException("Main Category with name "+ name + " already exists");
        }
        return false;
    }

    public void attachPictureToMainCat(MainCategory mainCategory, MultipartFile multipartFile,Path path) throws IOException {
        mainCategory.setImageUrl(multipartFile.getOriginalFilename());
        multipartFile.transferTo(Path.of(path + "/" + multipartFile.getOriginalFilename()));
        FileManager.transferMultipartFile(multipartFile,path);
    }
    public void create(MainCategory mainCategory, MultipartFile multipartFile, Path path) throws IOException {
        mainCategoryExists(mainCategory.getName());
        attachPictureToMainCat(mainCategory,multipartFile,path);
        mainCategoryRepository.save(mainCategory);
        mainCategoryRepository.flush();
    }

    public MainCategory getByIdForController(Long mainCatId) {
        Optional<MainCategory> optionalMainCategory = findById(mainCatId);
        mainCategoryIsNotFound(mainCatId);
        return optionalMainCategory.get();
    }

    public void update(Long mainCatId, MainCategoryUpdateDTO mainCategoryUpdateDTO,MultipartFile multipartFile,Path path) throws IOException {
        Optional<MainCategory> optionalMainCategory = mainCategoryRepository.findById(mainCatId);
        mainCategoryIsNotFound(mainCatId);
        mainCategoryExists(mainCategoryUpdateDTO.getNewName());

        MainCategory mainCategory = optionalMainCategory.get();
        mainCategory.setName(mainCategoryUpdateDTO.getNewName());
        FileManager.deleteFile(path + "/" + mainCategory.getImageUrl());
        attachPictureToMainCat(mainCategory,multipartFile,path);
        mainCategoryRepository.save(mainCategory);
        mainCategoryRepository.flush();
    }
}

