package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class ProductService {
    private final MainCategoryService mainCategoryService;
    private final ProductRepository productRepository;
    private final SubcategoryService subcategoryService;
    public ProductService(MainCategoryService mainCategoryService, ProductRepository productRepository, SubcategoryService subcategoryService) {
        this.mainCategoryService = mainCategoryService;
        this.productRepository = productRepository;
        this.subcategoryService = subcategoryService;
    }

    public List<Product> getProductsByMainCategoryId(long id) {
        Optional<MainCategory> mainCategory = mainCategoryService.findById(id);
        List<Product> products = new ArrayList<>();
        if (mainCategory.isPresent()) {
            MainCategory foundMainCategory = mainCategory.get();
            List<Subcategory> subcategories = foundMainCategory.getSubCategories();
            List<NestedCategory> nestedCategories = new ArrayList<>();
            for (Subcategory subcategory : subcategories) {
                nestedCategories.addAll(subcategory.getNestedCategories());
            }
            for(NestedCategory nestedCategory : nestedCategories) {
                products.addAll(nestedCategory.getProducts());
            }
        }
       return products;
    }
    public List<Product> getProductsByNestedCategoryId(long id) {
        return productRepository.getProductsByNestedCategoryId(id);
    }
    public List<Product> getProductsBySubCategoryId(long id) {
        List<Product> products = new ArrayList<>();
        Optional<Subcategory> subcategory = subcategoryService.findById(id);
        if (subcategory.isPresent()) {
               List<NestedCategory> nestedCategories = subcategory.get().getNestedCategories();
               for(NestedCategory nestedCategory : nestedCategories) {
                  products.addAll(nestedCategory.getProducts());
               }
        }
        return products;
    }

    public Map<Subcategory, List<NestedCategory>> getCategoriesFilter(long id){
        Map<Subcategory, List<NestedCategory>> map = new HashMap<>();
        List<Subcategory> subcategories = subcategoryService.findAllByMainCategoryId(id);
        for (Subcategory subcategory : subcategories) {
            map.put(subcategory,subcategory.getNestedCategories());
        }
        return map;
    }

}
