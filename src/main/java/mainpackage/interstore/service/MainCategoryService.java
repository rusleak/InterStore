package mainpackage.interstore.service;

import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.repository.MainCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainCategoryService {
    private final MainCategoryRepository mainCategoryRepository;
    @Autowired
    public MainCategoryService(MainCategoryRepository mainCategoryRepository) {
        this.mainCategoryRepository = mainCategoryRepository;
    }

    public List<MainCategory> findAll() {
        return mainCategoryRepository.findAll();
    }


    public Optional<MainCategory> findById(long id) {
        return mainCategoryRepository.findById(id);
    }
}
