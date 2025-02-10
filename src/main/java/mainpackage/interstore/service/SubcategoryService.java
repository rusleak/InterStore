package mainpackage.interstore.service;

import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
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



}
