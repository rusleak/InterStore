package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.repository.MainCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainCategoryService {
    private final MainCategoryRepository mainCategoryRepository;
    @Autowired
    public MainCategoryService(MainCategoryRepository mainCategoryRepository) {
        this.mainCategoryRepository = mainCategoryRepository;
    }

}
