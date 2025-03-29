package mainpackage.interstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.model.DTOs.NestedCategoryDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.repository.NestedCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NestedCategoryService {
    private final NestedCategoryRepository nestedCategoryRepository;
    private final SubcategoryService subcategoryService;
    @Autowired
    public NestedCategoryService(NestedCategoryRepository nestedCategoryRepository,SubcategoryService subcategoryService) {
        this.nestedCategoryRepository = nestedCategoryRepository;
        this.subcategoryService = subcategoryService;
    }

    public Optional<NestedCategory> findById(Long id) {
        return nestedCategoryRepository.findById(id);
    }

    public Optional<NestedCategory> findByName(String nestedCategoryName) {
        return nestedCategoryRepository.findByName(nestedCategoryName);
    }
    public boolean nestedCategoryExists(String name) {
        Optional<NestedCategory> optionalNestedCategory = nestedCategoryRepository.findByName(name);
        if(optionalNestedCategory.isPresent()) {
            throw new EntityExistsException("Nested Category with name "+ name + " already exists");
        }
        return false;
    }
    public NestedCategory nestedCategoryIsNotFound(Long nestedCategoryId) {
        Optional<NestedCategory> optionalNestedCategory = nestedCategoryRepository.findById(nestedCategoryId);
        if(optionalNestedCategory.isEmpty()) {
            throw new EntityNotFoundException("Nested category with id " + nestedCategoryId + " not found");
        }
        return optionalNestedCategory.get();
    }
    public List<NestedCategory> findAllBySubCatId(Long subCatId) {
       return nestedCategoryRepository.findAllBySubcategoryId(subCatId);
    }

    public void create(NestedCategoryDTO nestedCategoryDTO) {
        nestedCategoryExists(nestedCategoryDTO.getName());
        Subcategory subcategory = subcategoryService.subCategoryIsNotFound(nestedCategoryDTO.getSubCategoryId());
        NestedCategory nestedCategory = TransformerDTO.dtoToNestedCategory(nestedCategoryDTO,subcategory);
        nestedCategoryRepository.save(nestedCategory);
    }

    public void update(Long nestedCategoryId, NestedCategoryDTO nestedCategoryDTO) {
        Subcategory subcategory = subcategoryService.subCategoryIsNotFound(nestedCategoryDTO.getSubCategoryId());
        nestedCategoryExists(nestedCategoryDTO.getName());
        NestedCategory nestedCategory = nestedCategoryIsNotFound(nestedCategoryId);

        nestedCategory.setName(nestedCategoryDTO.getName());
        nestedCategory.setSubcategory(subcategory);
        nestedCategoryRepository.save(nestedCategory);
    }

    public void delete(Long nestedCatId) {
            NestedCategory nestedCategory = nestedCategoryIsNotFound(nestedCatId);
            if(nestedCategory.getProducts().size() > 0) {
                throw new EntityExistsException("Can't delete nested category when it have products inside");
            }
            nestedCategoryRepository.delete(nestedCategory);
    }
    public NestedCategory loadNestedCategory(Long nestedCategoryId) {
        Optional<NestedCategory> optionalNestedCategory = nestedCategoryRepository.findById(nestedCategoryId);
        return optionalNestedCategory.orElse(new NestedCategory());  // Если не найдено, возвращаем новый объект
    }
}
