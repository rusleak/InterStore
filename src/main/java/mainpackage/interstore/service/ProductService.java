package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
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
    public double[] getMinAndMaxPriceFromProductList(List<Product> productList) {
        double[] result = new double[2];
            OptionalDouble maxPrice = productList.stream()
                    .mapToDouble(product -> product.getPrice().doubleValue())
                    .max();

        OptionalDouble minPrice = productList.stream()
                .mapToDouble(product -> product.getPrice().doubleValue())
                .min();

        if(minPrice.isPresent()) {
            result[0] = minPrice.getAsDouble();
        } else {
            result[0] = 0;
        }
            if(maxPrice.isPresent()) {
                result[1] = maxPrice.getAsDouble()+1;
            } else {
                result[1] = 100000;
            }
            return result;
    }
    public List<Product> getAllProductsByPriceRange(List<Product> productList,Long minPrice, Long maxPrice) {
        List<Product> newProductList = new ArrayList<>();
        BigDecimal minPriceDecimal = new BigDecimal(minPrice);
        BigDecimal maxPriceDecimal = new BigDecimal(maxPrice);
        for (Product product : productList) {
            if (product.getPrice().compareTo(minPriceDecimal) >= 0 && product.getPrice().compareTo(maxPriceDecimal) <= 0) {
                newProductList.add(product);
            }
        }
        return newProductList;
    }
    public List<Product> getAllProductsByPriceRange(List<Product> productList, List<String> priceRanges) {
        List<Product> newProductList = new ArrayList<>();

        // Проверяем на null и пустоту списка
        if (priceRanges != null && !priceRanges.isEmpty()) {
            for (String range : priceRanges) {
                String[] bounds = range.split("-");
                long minPrice = Long.parseLong(bounds[0]);
                long maxPrice = Long.parseLong(bounds[1]);

                // Получаем товары для каждого диапазона цен
                List<Product> temporaryProducts = getAllProductsByPriceRange(productList, minPrice, maxPrice);

                // Добавляем товары в новый список, избегая дублирования
                for (Product product : temporaryProducts) {
                    if (!newProductList.contains(product)) {
                        newProductList.add(product);
                    }
                }
            }
        }
        return newProductList;
    }

    public List<Product> filterProductsByColors(List<Product> products, List<Long> colorIds) {
        if (colorIds == null || colorIds.isEmpty()) {
            return products; // Если фильтр не выбран – возвращаем все товары
        }

        return products.stream()
                .filter(product -> product.getColors().stream()
                        .anyMatch(color -> colorIds.contains(color.getId())))
                .toList();
    }


}
