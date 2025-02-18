package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.model.*;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public TreeMap<Subcategory, List<NestedCategory>> getCategoriesFilter(long id){
        TreeMap<Subcategory, List<NestedCategory>> map = new TreeMap<>();
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
                result[1] = maxPrice.getAsDouble();
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

        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getColors() != null) {
                for (Color color : product.getColors()) {
                    if (colorIds.contains(color.getId())) {
                        filteredProducts.add(product);
                        break; // Если хотя бы один цвет совпал, добавляем товар и выходим из цикла
                    }
                }
            }
        }

        return filteredProducts;
    }


    public List<Product> getProductsFromMinToMaxPrice(List<Product> products, String filterMinPrice, String filterMaxPrice) {
        if (products != null && !products.isEmpty()) {
            // Фильтрация по минимальной цене
            if (filterMinPrice != null && !filterMinPrice.trim().isEmpty()) {
                try {
                    BigDecimal min = new BigDecimal(filterMinPrice).subtract(BigDecimal.valueOf(1));
                    System.out.println("min method : " + min);
                    products = products.stream()
                            .filter(product -> product.getPrice().compareTo(min) >= 0)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Некорректное значение минимальной цены: " + filterMinPrice, e);
                }
            }

            // Фильтрация по максимальной цене
            if (filterMaxPrice != null && !filterMaxPrice.trim().isEmpty()) {
                try {
                    BigDecimal max = new BigDecimal(filterMaxPrice).add(BigDecimal.valueOf(1));
                    System.out.println("max method : " + max);
                    products = products.stream()
                            .filter(product -> product.getPrice().compareTo(max) <= 0)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Некорректное значение максимальной цены: " + filterMaxPrice, e);
                }
            }
        }
        return products;
    }
}
