package mainpackage.interstore.Services;

import mainpackage.interstore.Entities.Product;
import mainpackage.interstore.Entities.Subcategory;
import mainpackage.interstore.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<Long> findMinAndMaxPrice(List<Product> productList) {
        if (productList == null || productList.isEmpty()) {
            throw new IllegalArgumentException("Products list cannot be null or empty");
        }

        long minPrice = Long.MAX_VALUE;
        long maxPrice = Long.MIN_VALUE;

        for (Product product : productList) {
            BigDecimal price = product.getPrice();
            Long priceLong = price.toBigInteger().longValue();
            if (priceLong < minPrice) {
                minPrice = priceLong;
            }
            if (priceLong > maxPrice) {
                maxPrice = priceLong+1;
            }
        }

        List<Long> result = new ArrayList<>();
        result.add(minPrice);
        result.add(maxPrice);

        return result;
    }
}
