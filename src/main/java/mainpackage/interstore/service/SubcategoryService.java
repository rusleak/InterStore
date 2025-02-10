package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }


}
