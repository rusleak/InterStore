package mainpackage.interstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.model.util.NestedCategoryDTO;
import mainpackage.interstore.repository.NestedCategoryRepository;
import mainpackage.interstore.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NestedCategoryService {
    private final NestedCategoryRepository nestedCategoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    @Autowired
    public NestedCategoryService(NestedCategoryRepository nestedCategoryRepository, SubcategoryRepository subcategoryRepository) {
        this.nestedCategoryRepository = nestedCategoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    Optional<NestedCategory> findById(Long id) {
        return nestedCategoryRepository.findById(id);
    }

    public Optional<NestedCategory> findByName(String nestedCategoryName) {
        return nestedCategoryRepository.findByName(nestedCategoryName);
    }

    public void receiveNestedCategory(NestedCategoryDTO nestedCategoryDTO) {
        Optional<NestedCategory> optionalNestedCategory = nestedCategoryRepository.findByName(nestedCategoryDTO.getName());
        Optional<Subcategory> optionalSubcategory = subcategoryRepository.findById(nestedCategoryDTO.getSubcategoryId());
        Optional<NestedCategory> optNestedCategoryNewName = nestedCategoryRepository.findByName(nestedCategoryDTO.getNewName());

        NestedCategory nestedCategory = new NestedCategory();
        if(optNestedCategoryNewName.isPresent()) {
            throw new EntityExistsException("Nested-category with provided new name already exists");
        }
        if (optionalSubcategory.isEmpty()) {
            throw new EntityNotFoundException("Subcategory category with such id not found");
        }
        if (optionalNestedCategory.isPresent()) {
            nestedCategory = optionalNestedCategory.get();
        }

        if(nestedCategoryDTO.getNewName() != null) {
            nestedCategory.setName(nestedCategoryDTO.getNewName());
        } else {
            nestedCategory.setName(nestedCategoryDTO.getName());
        }
        nestedCategory.setSubcategory(optionalSubcategory.get());
        nestedCategoryRepository.save(nestedCategory);
    }
}
