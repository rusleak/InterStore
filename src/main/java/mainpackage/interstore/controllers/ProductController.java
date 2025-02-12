package mainpackage.interstore.controllers;

import mainpackage.interstore.model.*;
import mainpackage.interstore.priceUtils.PriceUtils;
import mainpackage.interstore.service.ColorService;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("")
@SessionAttributes({"availableColors"})
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
                       @RequestParam(required = false) List<String> priceRange,
                       @RequestParam(required = false) List<Long> colors,
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
        // Available colors for current product list
        model.addAttribute("availableColors", availableColors);
        // Preserve price range filters
        model.addAttribute("priceRangeFilters", priceRange);

        // Price filtering
        double[] minAndMaxPrice = productService.getMinAndMaxPriceFromProductList(products);
        Integer minPrice = (int) Math.floor(minAndMaxPrice[0]);
        Integer maxPrice = (int) Math.floor(minAndMaxPrice[1]);
        model.addAttribute("minPriceFromMainCategory", minPrice);
        model.addAttribute("maxPriceFromMainCategory", maxPrice);

        if (priceRange != null && !priceRange.isEmpty()) {
            products = productService.getAllProductsByPriceRange(products, priceRange);
        }

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
        Map<Subcategory, List<NestedCategory>> categoryFilters = productService.getCategoriesFilter(id);
        model.addAttribute("categoryFilters", categoryFilters);
        model.addAttribute("mainCategoryId", id);

        // Price ranges for filter checkboxes
        List<String> priceRanges = PriceUtils.calculatePriceRanges(minPrice, maxPrice);
        model.addAttribute("priceRanges", priceRanges);

        return "products";
    }
}