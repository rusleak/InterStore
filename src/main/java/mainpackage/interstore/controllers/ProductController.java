package mainpackage.interstore.controllers;

import mainpackage.interstore.model.*;
import mainpackage.interstore.service.ColorService;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//TODO Фильтрация по производителю
//TODO Какая категория активна добавить строку
//TODO Навести порядок в продукт сервисе
@Controller
@RequestMapping("")
public class ProductController {
    private final MainCategoryService mainCategoryService;
    private final ProductService productService;
    private final ColorService colorService;

    @Autowired
    public ProductController(MainCategoryService mainCategoryService, ProductService productService, ColorService colorService) {
        this.mainCategoryService = mainCategoryService;
        this.productService = productService;
        this.colorService = colorService;
    }

    @GetMapping("/main-category/{id}")
    String getProducts(@PathVariable("id") Long id,
                       @RequestParam(required = false) Long subcategoryId,
                       @RequestParam(required = false) Long nestedCategoryId,
                       @RequestParam(required = false) String filterMinPrice,
                       @RequestParam(required = false) String filterMaxPrice,
                       @RequestParam(required = false) List<Long> colors,
                       @RequestParam(required = false) List<String> dimensions,
                       @RequestParam(required = false) List<String> tagsFromClient,
                       @RequestParam(required = false) List<String> brands,
                       Model model) {
        model.addAttribute("mainCategoryId", id);
        model.addAttribute("nestedCategoryId", nestedCategoryId);
        model.addAttribute("subcategoryId", subcategoryId);


        //Filtered products
        List<Product> products = productService.getFilteredProducts(id, subcategoryId, nestedCategoryId);


        // Available colors for current product list
        List<Color> availableColors = colorService.getAvailableColors(products);
        if(availableColors != null) {
            Collections.sort(availableColors);
        }
        // Available colors for current product list
        model.addAttribute("availableColors", availableColors);


        // Brand filtering
        model.addAttribute("availableBrands",productService.getAllBrandsFromProducts(products));
        model.addAttribute("selectedBrands",brands);


        //Firstly get dimensions and tags from subcategory/nestedcategory product list to display them all
        if (subcategoryId != null) {
            model.addAttribute("availableDimensions", productService.getAllDimensionsFromProducts(products));
            model.addAttribute("availableTags",productService.getAllTagsFromProducts(products));
        }

        //Filtering by dimensions
        products = productService.filterByDimensions(products, dimensions);
        //Filtering by tags
        products = productService.filterProductsByTags(products,tagsFromClient);
        model.addAttribute("selectedTags", tagsFromClient);
        model.addAttribute("selectedDimensions", dimensions);


        //1 Preserve price range filters
        model.addAttribute("filterMinPrice", filterMinPrice);
        model.addAttribute("filterMaxPrice", filterMaxPrice);

        //2 Price placeholders
        double[] minAndMaxPrice = productService.getMinAndMaxPriceFromProductList(products);
        Integer minPrice = (int) Math.floor(minAndMaxPrice[0]);
        Integer maxPrice = (int) Math.floor(minAndMaxPrice[1]);
        model.addAttribute("placeholderFromPrice", minPrice);
        model.addAttribute("placeholderToPrice", maxPrice);

        //3 Products in price range
        products = productService.getProductsFromMinToMaxPrice(products,filterMinPrice,filterMaxPrice);

        // Preserve color filters
        model.addAttribute("selectedColors", colors);

        // Color filtering
        if (colors != null && !colors.isEmpty()) {
            products = productService.filterProductsByColors(products, colors);
        }

        products = productService.filterProductsByBrands(products,brands);

        // Sorting by decreasing price
        products.sort(Comparator.comparing(Product::getPrice).reversed());
        model.addAttribute("productsList", products);

        // Category filters
        model.addAttribute("categoryFilters", productService.getCategoriesFilter(id));




        return "products";
    }
}