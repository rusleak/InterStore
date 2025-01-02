package mainpackage.interstore.Services;

import mainpackage.interstore.Entities.Product;
import mainpackage.interstore.Entities.Subcategory;
import mainpackage.interstore.Repositories.SubcategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SubcategoryService {
    private final SubcategoryRepo subcategoryRepo;
    @Autowired
    public SubcategoryService(SubcategoryRepo subcategoryRepo) {
        this.subcategoryRepo = subcategoryRepo;
    }

    public List<Subcategory> getSubcategoriesByCategoryId(Long id){
        return subcategoryRepo.getSubcategoriesByCategory_Id(id);
    }

    public Optional<Subcategory> findById(Long id) {
        return subcategoryRepo.findById(id);
    }


}
