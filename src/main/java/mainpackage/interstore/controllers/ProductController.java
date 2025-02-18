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

//TODO Реализовать tag List<Tag> tagList
//TODO Реализовать sizes private String dimensions;
//TODO подумать как Реализовать dimensons как лист
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
                       Model model) {
        model.addAttribute("nestedCategoryId", nestedCategoryId);
        model.addAttribute("subcategoryId", subcategoryId);


        // Available colors for current product list
        List<Color> availableColors;
        List<Product> products;
        if (nestedCategoryId != null) {
            products = productService.getProductsByNestedCategoryId(nestedCategoryId);
            availableColors = colorService.getAvailableColors(products);
        } else if (subcategoryId != null) {
            products = productService.getProductsBySubCategoryId(subcategoryId);
            availableColors = colorService.getAvailableColors(products);
        } else {
            products = productService.getProductsByMainCategoryId(id);
            availableColors = colorService.getAvailableColors(products);
        }

        //Filtering by dimensions
        Set<String> availableDimensions = productService.getAllDimensionsFromProducts(products);
        model.addAttribute("availableDimensions",availableDimensions);
        if(dimensions != null) {
            if(!dimensions.isEmpty()) {
                products = productService.getAllProductsByGivenDimensions(products, dimensions);
            }
        }
        model.addAttribute("selectedDimensions", dimensions);



        if(availableColors != null) {
            Collections.sort(availableColors);
        }
        // Available colors for current product list
        model.addAttribute("availableColors", availableColors);
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



        // Sorting by decreasing price
        products.sort(Comparator.comparing(Product::getPrice).reversed());
        model.addAttribute("productsList", products);

        // Category filters
        TreeMap<Subcategory, List<NestedCategory>> categoryFilters = productService.getCategoriesFilter(id);
        model.addAttribute("categoryFilters", categoryFilters);
        model.addAttribute("mainCategoryId", id);



        return "products";
    }
}