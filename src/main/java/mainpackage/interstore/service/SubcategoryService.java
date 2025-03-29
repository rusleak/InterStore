package mainpackage.interstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.model.DTOs.SubCategoryDTO;
import mainpackage.interstore.model.DTOs.SubCategoryUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
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
        mainCategoryIsNotFound(id);
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


    public Subcategory subCategoryIsNotFound(Long subCatId) {
        Optional<Subcategory> optionalSubCategory = findById(subCatId);
        if(optionalSubCategory.isEmpty()) {
            throw new EntityNotFoundException("Subcategory with id " + subCatId + "not found");
        }
        return optionalSubCategory.get();
    }
    public boolean subCategoryExists(String name) {
        Optional<Subcategory> optionalSubCategory = subcategoryRepository.findByName(name);
        if(optionalSubCategory.isPresent()) {
            throw new EntityExistsException("Sub Category with name "+ name + " already exists");
        }
        return false;
    }
    public MainCategory mainCategoryIsNotFound(Long mainCatId) {
        Optional<MainCategory> optionalMainCategory = mainCategoryRepository.findById(mainCatId);
        if(optionalMainCategory.isEmpty()) {
            throw new EntityNotFoundException("Main category with id " + mainCatId + " not found");
        }
        return optionalMainCategory.get();
    }
    public void create(SubCategoryDTO subCategoryDTO) {
        subCategoryExists(subCategoryDTO.getName());
        MainCategory mainCategory = mainCategoryIsNotFound(subCategoryDTO.getMainCategoryId());

        Subcategory subcategory = TransformerDTO.dtoToSubCategory(subCategoryDTO,mainCategory);
        subcategoryRepository.save(subcategory);

    }

    public void update(Long subCatId, SubCategoryUpdateDTO subCategoryUpdateDTO) {
        MainCategory mainCategory = mainCategoryIsNotFound(subCategoryUpdateDTO.getNewMainCategoryId());
        subCategoryExists(subCategoryUpdateDTO.getNewName());
        Subcategory subcategory = subCategoryIsNotFound(subCatId);


        subcategory.setName(subCategoryUpdateDTO.getNewName());
        subcategory.setMainCategory(mainCategory);
        subcategoryRepository.save(subcategory);
    }

    public void delete(Long subCatId) {
        Subcategory subcategory = subCategoryIsNotFound(subCatId);
        if(subcategory.getNestedCategories().size() > 0) {
            throw new EntityExistsException("Can't delete subcategory when it have nested categories inside");
        }
        subcategoryRepository.delete(subcategory);
    }
}
