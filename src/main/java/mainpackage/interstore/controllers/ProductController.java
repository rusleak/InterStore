package mainpackage.interstore.controllers;

import mainpackage.interstore.model.*;
import mainpackage.interstore.model.DTOs.ProductFilterDTO;
import mainpackage.interstore.repository.ProductRepository;
import mainpackage.interstore.service.ColorService;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import mainpackage.interstore.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("")
public class ProductController {
    private final MainCategoryService mainCategoryService;
    private final ProductService productService;
    private final ColorService colorService;
    private final SubcategoryService subcategoryService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(MainCategoryService mainCategoryService, ProductService productService, ColorService colorService, SubcategoryService subcategoryService,
                             ProductRepository productRepository) {
        this.mainCategoryService = mainCategoryService;
        this.productService = productService;
        this.colorService = colorService;
        this.subcategoryService = subcategoryService;
        this.productRepository = productRepository;
    }

    @GetMapping("/main-category/{id}")
    public String getProducts(@PathVariable("id") Long id,
                              @ModelAttribute ProductFilterDTO productFilterDTO,
                              Model model) {
        model.addAttribute("mainCategoryId", id);
        model.addAttribute("nestedCategoryId", productFilterDTO.getNestedCategoryId());
        model.addAttribute("subcategoryId", productFilterDTO.getSubcategoryId());

        //Filtered products
        List<Product> products = productService.getFilteredProducts(id, productFilterDTO.getSubcategoryId(), productFilterDTO.getNestedCategoryId());
        products = productService.excludeNullPictures(products);

        // Available colors for current product list
        List<Color> availableColors = colorService.getAvailableColors(products);
        if (availableColors != null) {
            Collections.sort(availableColors);
        }
        model.addAttribute("availableColors", availableColors);

        // Brand filtering
        model.addAttribute("availableBrands", productService.getAllBrandsFromProducts(products));
        model.addAttribute("selectedBrands", productFilterDTO.getBrands());

        // Firstly get dimensions and tags from subcategory/nestedcategory product list to display them all
        if (productFilterDTO.getSubcategoryId() != null) {
            model.addAttribute("availableDimensions", productService.getAllDimensionsFromProducts(products));
            model.addAttribute("availableTags", productService.getAllTagsFromProducts(products));
        }

        // Filtering by dimensions
        products = productService.filterByDimensions(products, productFilterDTO.getDimensions());
        // Filtering by tags
        products = productService.filterProductsByTags(products, productFilterDTO.getTagsFromClient());
        model.addAttribute("selectedTags", productFilterDTO.getTagsFromClient());
        model.addAttribute("selectedDimensions", productFilterDTO.getDimensions());

        // Preserve price range filters
        model.addAttribute("filterMinPrice", productFilterDTO.getFilterMinPrice());
        model.addAttribute("filterMaxPrice", productFilterDTO.getFilterMaxPrice());

        // Price placeholders
        double[] minAndMaxPrice = productService.getMinAndMaxPriceFromProductList(products);
        Integer minPrice = (int) Math.floor(minAndMaxPrice[0]);
        Integer maxPrice = (int) Math.floor(minAndMaxPrice[1]);
        model.addAttribute("placeholderFromPrice", minPrice);
        model.addAttribute("placeholderToPrice", maxPrice);

        // Products in price range
        products = productService.getProductsFromMinToMaxPrice(products, productFilterDTO.getFilterMinPrice(), productFilterDTO.getFilterMaxPrice());

        // Preserve color filters
        model.addAttribute("selectedColors", productFilterDTO.getColors());

        // Color filtering
        if (productFilterDTO.getColors() != null && !productFilterDTO.getColors().isEmpty()) {
            products = productService.filterProductsByColors(products, productFilterDTO.getColors());
        }

        products = productService.filterProductsByBrands(products, productFilterDTO.getBrands());

        // Sorting by decreasing price
        products.sort(Comparator.comparing(Product::getPrice).reversed());
        model.addAttribute("productsList", products);

        // Category filters
        model.addAttribute("categoryFilters", subcategoryService.getCategoriesFilter(id));

        // Adding current active category
        model.addAttribute("activeCategory", mainCategoryService.getActiveCategory(id, productFilterDTO.getSubcategoryId(), productFilterDTO.getNestedCategoryId(), products));

        return "products";
    }

    @GetMapping("product/{id}")
    public String getProduct(@PathVariable("id") Long id,
                             Model model) {
        productService.fillTheModelProductPage(model, id);
        return "productPage";
    }
}