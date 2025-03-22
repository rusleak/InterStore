package mainpackage.interstore.service;

import mainpackage.interstore.model.*;
import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.util.ProductReceiverDTO;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final MainCategoryService mainCategoryService;
    private final NestedCategoryService nestedCategoryService;
    private final ProductRepository productRepository;
    private final SubcategoryService subcategoryService;
    private final DimensionsService dimensionsService;
    private final ColorService colorService;
    private final TagService tagService;



    public ProductService(MainCategoryService mainCategoryService, NestedCategoryService nestedCategoryService, ProductRepository productRepository, SubcategoryService subcategoryService, DimensionsService dimensionsService, ColorService colorService, TagService tagService) {
        this.mainCategoryService = mainCategoryService;
        this.nestedCategoryService = nestedCategoryService;
        this.productRepository = productRepository;
        this.subcategoryService = subcategoryService;
        this.dimensionsService = dimensionsService;
        this.colorService = colorService;
        this.tagService = tagService;
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

    public List<Product> excludeNullPictures(List<Product> products) {
        if (products != null || !products.isEmpty()) {
            // Фильтруем продукты, у которых список изображений пуст или равен null
            return products.stream()
                    .filter(product -> product.getProductImages() != null && !product.getProductImages().isEmpty())
                    .collect(Collectors.toList());
        } else {
            // Если список продуктов пустой или равен null, возвращаем его без изменений
            return products;
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

   ////////////////////PRODUCT PAGE///////////////
   public Product findById(Long productId) {
       return productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("No product found with ID: " + productId));
   }




    public void fillTheModelProductPage(Model model, Long id) {
        if(id != null) {
        Product product = findById(id);
        model.addAttribute("productName",product.getName());
        model.addAttribute("productBrand",product.getBrand());
        model.addAttribute("productDescription",product.getDescription());
        model.addAttribute("productColors",product.getColors());
        model.addAttribute("productDimensions",product.getDimensions());
        model.addAttribute("productId",product.getOneCId().toString());
        try {

            model.addAttribute("productDiscountedPrice",product.getDiscountedPrice().toString() + " грн");
        }catch (NullPointerException e) {

        }
        model.addAttribute("productPrice",product.getPrice().toString() + " грн");
        model.addAttribute("productTags",product.getTagList());
        model.addAttribute("productStockQuantity",product.getStockQuantity().toString());
        model.addAttribute("productImages",product.getProductImages());
        }
    }

    private void enrichProductWithRelations(Product product, List<Color> colors, List<Tag> tags, List<Dimensions> dimensions, NestedCategory nestedCategory) {
        product.setColors(colors);
        product.setTagList(tags);
        product.setDimensions(dimensions);
        product.setNestedCategory(nestedCategory);
    }

    public Product fillStaticFieldsOfProductFromDTO(ProductReceiverDTO productReceiverDTO, Product product) {
        product.setPrice(productReceiverDTO.getPrice());
        product.setName(productReceiverDTO.getName());
        product.setDescription(productReceiverDTO.getDescription());
        product.setDiscountedPrice(productReceiverDTO.getDiscountedPrice());
        product.setStockQuantity(productReceiverDTO.getStockQuantity());
        product.setProductImages(productReceiverDTO.getProductImages());
        product.setBrand(productReceiverDTO.getBrand());
        return product;
    }

    private void validateProductDTOCollections(ProductReceiverDTO dto) throws Exception {
        if (dto.getColors() == null || dto.getColors().isEmpty()) {
            throw new Exception("Colors list is empty");
        }
        if (dto.getTagList() == null || dto.getTagList().isEmpty()) {
            throw new Exception("Tag list is empty");
        }
        if (dto.getDimensions() == null || dto.getDimensions().isEmpty()) {
            throw new Exception("Dimensions list is empty");
        }
        if (dto.getNestedCategory() == null || dto.getNestedCategory().isBlank()) {
            throw new Exception("Nested category is missing");
        }
    }

    public void handleReceivedProduct(ProductReceiverDTO dto) throws Exception {

        Set<String> colorNames = new HashSet<>(dto.getColors());
        Set<String> tagNames = new HashSet<>(dto.getTagList());
        Set<String> dimensionNames = new HashSet<>(dto.getDimensions());

        validateProductDTOCollections(dto);


        List<Color> colorsFromDb = colorService.findByNameIn(colorNames);
        List<Tag> tagsFromDb = tagService.findByNameIn(tagNames);
        List<Dimensions> dimensionsFromDb = dimensionsService.findByNameIn(dimensionNames);

        NestedCategory nestedCategory = nestedCategoryService.findByName(dto.getNestedCategory())
                .orElseThrow(() -> new Exception("Nested category not passed but obligatory"));

        Product product = productRepository.findByOneCId(dto.getOneCId())
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setOneCId(dto.getOneCId());  // Устанавливаем oneCId
                    return newProduct;
                });



        // Логика с удалением старых картинок, если они не присутствуют в новом списке
        if (product.getId() != null) {
            List<String> existingImages = new ArrayList<>(product.getProductImages());
            List<String> newImages = dto.getProductImages();

            // Находим картинки, которых нет в новых данных и удаляем их
            for (String oldImage : existingImages) {
                if(newImages.contains(oldImage)) {

                } else  {
                    // Удаляем файл с диска
                    Path pathToDelete = Paths.get(oldImage);
                    Files.deleteIfExists(pathToDelete);
                }
            }
        }


        fillStaticFieldsOfProductFromDTO(dto, product);
        enrichProductWithRelations(product, colorsFromDb, tagsFromDb, dimensionsFromDb, nestedCategory);

        if (product.getOneCId() == null) {
            throw new IllegalArgumentException("oneCId cannot be null");
        }

        productRepository.save(product);
    }
}