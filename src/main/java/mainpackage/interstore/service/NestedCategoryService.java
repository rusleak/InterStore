package mainpackage.interstore.service;

import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.repository.NestedCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NestedCategoryService {
    private final NestedCategoryRepository nestedCategoryRepository;
    @Autowired
    public NestedCategoryService(NestedCategoryRepository nestedCategoryRepository) {
        this.nestedCategoryRepository = nestedCategoryRepository;
    }

    Optional<NestedCategory> findById(Long id) {
        return nestedCategoryRepository.findById(id);
    }

    public Optional<NestedCategory> findByName(String nestedCategoryName) {
        return nestedCategoryRepository.findByName(nestedCategoryName);
    }
}
