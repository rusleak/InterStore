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
            for (NestedCategory nestedCategory : nestedCategories) {
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
            for (NestedCategory nestedCategory : nestedCategories) {
                products.addAll(nestedCategory.getProducts());
            }
        }
        return products;
    }



    public double[] getMinAndMaxPriceFromProductList(List<Product> productList) {
        double[] result = new double[2];
        OptionalDouble maxPrice = productList.stream()
                .mapToDouble(product -> product.getPrice().doubleValue())
                .max();

        OptionalDouble minPrice = productList.stream()
                .mapToDouble(product -> product.getPrice().doubleValue())
                .min();

        if (minPrice.isPresent()) {
            result[0] = minPrice.getAsDouble();
        } else {
            result[0] = 0;
        }
        if (maxPrice.isPresent()) {
            result[1] = maxPrice.getAsDouble();
        } else {
            result[1] = 100000;
        }
        return result;
    }

    public List<Product> filterProductsByColors(List<Product> products, List<Long> colorIds) {
        if (colorIds == null || colorIds.isEmpty() || products == null || products.isEmpty()) {
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

    public List<Product> getAllProductsByGivenDimensions(List<Product> productList, List<String> dimensions) {
        return productList.stream()
                .filter(product -> product.getDimensions().stream()
                        .map(Dimensions::getSize)
                        .anyMatch(dimensions::contains)) // Проверяем, есть ли пересечение размеров
                .collect(Collectors.toList());
    }

    public List<Product> getFilteredProducts(Long mainCategoryId, Long subcategoryId, Long nestedCategoryId) {
        if (nestedCategoryId != null) {
            return getProductsByNestedCategoryId(nestedCategoryId);
        } else if (subcategoryId != null) {
            return getProductsBySubCategoryId(subcategoryId);
        } else {
            return getProductsByMainCategoryId(mainCategoryId);
        }
    }

    public List<Product> filterByDimensions(List<Product> products, List<String> dimensions) {
        if (dimensions != null && !dimensions.isEmpty()) {
            return getAllProductsByGivenDimensions(products, dimensions);
        }
        return products;
    }

    public TreeSet<String> getAllDimensionsFromProducts(List<Product> productList) {
        return productList.stream()
                .flatMap(product -> product.getDimensions().stream()) // Разворачиваем список размеров
                .filter(Objects::nonNull) // Фильтруем `null`
                .map(Dimensions::getSize) // Преобразуем `Dimension` в `String`
                .collect(Collectors.toCollection(TreeSet::new)); // Собираем в `TreeSet`
    }

    public List<Product> filterProductsByTags(List<Product> productList, List<String> tags) {
        if (productList == null || tags == null || tags.isEmpty()) {
            return productList != null ? productList : Collections.emptyList();
        }
        return productList.stream()
                .filter(product -> product.getTagList().stream()
                        .map(Tag::getName)
                        .anyMatch(tags::contains))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public TreeSet<String> getAllTagsFromProducts(List<Product> productList) {
        return productList.stream()
                .flatMap(product -> Optional.ofNullable(product.getTagList()) // Безопасно обрабатываем null
                        .stream()
                        .flatMap(List::stream)
                )
                .filter(Objects::nonNull) // Убираем возможные null-значения
                .map(Tag::getName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<String> getAllBrandsFromProducts(List<Product> productList) {
        return productList.stream()
                .map(Product::getBrand)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<Product> filterProductsByBrands(List<Product> productList, List<String> brands) {
        if (productList == null || brands == null || brands.isEmpty()) {
            return productList;
        }
        return productList.stream()
                .filter(product -> brands.contains(product.getBrand()))  // Проверяем, есть ли бренд в списке брендов
                .collect(Collectors.toList());  // Собираем отфильтрованные продукты в список
    }



}