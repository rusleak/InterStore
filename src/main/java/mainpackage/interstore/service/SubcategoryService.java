package mainpackage.interstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.model.util.SubCategoryDTO;
import mainpackage.interstore.repository.MainCategoryRepository;
import mainpackage.interstore.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;
    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository, MainCategoryRepository mainCategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;

        this.mainCategoryRepository = mainCategoryRepository;
    }
    public Optional<Subcategory> findById(long id) {
        return subcategoryRepository.findById(id);
    }
    public List<Subcategory> findAll() {
        return subcategoryRepository.findAll();
    }
    public List<Subcategory> findAllByMainCategoryId(long id) {
        return subcategoryRepository.findAllByMainCategoryId(id);
    }
    public TreeMap<Subcategory, List<NestedCategory>> getCategoriesFilter(long id) {
        TreeMap<Subcategory, List<NestedCategory>> map = new TreeMap<>();
        List<Subcategory> subcategories = findAllByMainCategoryId(id);
        for (Subcategory subcategory : subcategories) {
            map.put(subcategory, subcategory.getNestedCategories());
        }
        return map;
    }


    public void receiveSubCategory(SubCategoryDTO subCategoryDTO) {
        Optional<Subcategory> optSubcategory = subcategoryRepository.findByName(subCategoryDTO.getName());
        Optional<Subcategory> optSubcategoryNewName = subcategoryRepository.findByName(subCategoryDTO.getNewName());
        Optional<MainCategory> optMainCategory = mainCategoryRepository.findById(subCategoryDTO.getMainCategoryId());
        Subcategory foundSubcategory = new Subcategory();
        if(optSubcategoryNewName.isPresent()) {
            throw new EntityExistsException("Subcategory with provided new name already exists");
        }
        if (optMainCategory.isEmpty()) {
            throw new EntityNotFoundException("main category with such id not found");
        }

        if (optSubcategory.isPresent()) {
            foundSubcategory = optSubcategory.get();
        }
        if(subCategoryDTO.getNewName() != null) {
            foundSubcategory.setName(subCategoryDTO.getNewName());
        }

        foundSubcategory.setMainCategory(optMainCategory.get());
        subcategoryRepository.save(foundSubcategory);
    }
}
