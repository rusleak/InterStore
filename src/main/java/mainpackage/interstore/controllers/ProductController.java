package mainpackage.interstore.controllers;

import mainpackage.interstore.model.*;
import mainpackage.interstore.priceUtils.PriceUtils;
import mainpackage.interstore.service.ColorService;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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
                       @RequestParam(required = false) List<String> priceRange,
                       @RequestParam(required = false) List<Long> colors,
                       Model model) {
        model.addAttribute("nestedCategoryId",nestedCategoryId);
        model.addAttribute("subcategoryId", subcategoryId);
        List<Product> products;
        if (nestedCategoryId !=null) {
            products = productService.getProductsByNestedCategoryId(nestedCategoryId);
        } else if (subcategoryId != null) {
            products = productService.getProductsBySubCategoryId(subcategoryId);
        } else {
            products = productService.getProductsByMainCategoryId(id);
        }

        //Что бы чекбоксы оставались на месте после их выбора
        model.addAttribute("priceRangeFilters", priceRange);

        //Price
        double[] minAndMaxPrice = productService.getMinAndMaxPriceFromProductList(products);
        Integer minPrice = (int) Math.floor(minAndMaxPrice[0]);
        Integer maxPrice = (int) Math.floor(minAndMaxPrice[1]);
        model.addAttribute("maxPriceFromMainCategory",minPrice);
        model.addAttribute("maxPriceFromMainCategory",maxPrice);
        if(priceRange != null) {
            if (!priceRange.isEmpty()) {
                products = productService.getAllProductsByPriceRange(products,priceRange);
            }
        }

        //Colors
        // Фильтрация по цветам (после всех других фильтров)
        if (colors != null && !colors.isEmpty()) {
            products = productService.filterProductsByColors(products, colors);
        }
        List<Color> availableColors = colorService.getAvailableColors(products);
        model.addAttribute("availableColors", availableColors);
        model.addAttribute("selectedColors", colors); // Передаём уже выбранные фильтры

        //Sorting by decreasing price
        products.sort(Comparator.comparing(Product::getPrice).reversed());
        model.addAttribute("productsList", products);


        //Category
        Map<Subcategory, List<NestedCategory>> categoryFilters = productService.getCategoriesFilter(id);
        model.addAttribute("categoryFilters",categoryFilters);
        model.addAttribute("mainCategoryId", id);

        //
        List<String> priceRanges = PriceUtils.calculatePriceRanges(minPrice, maxPrice);
        model.addAttribute("priceRanges", priceRanges);


        return "products";
    }
}
