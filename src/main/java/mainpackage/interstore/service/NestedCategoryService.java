package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.repository.NestedCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NestedCategoryService {
    private final NestedCategoryRepository nestedCategoryRepository;
    @Autowired
    public NestedCategoryService(NestedCategoryRepository nestedCategoryRepository) {
        this.nestedCategoryRepository = nestedCategoryRepository;
    }
}
