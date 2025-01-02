package mainpackage.interstore.Services;

import mainpackage.interstore.Entities.Product;
import mainpackage.interstore.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepo productRepo;
    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll(){
        return productRepo.findAll();
    }
    public List<Product> findAllByCategoryId(Long id){
        return productRepo.findAllByCategoryId(id);
    }

    public List<Product> findAllBySubcategoryId(Long id) {
        return productRepo.findAllBySubcategoryId(id);
    }
}
