package mainpackage.interstore.controllers;

import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/main-category/{id}")
    String getProducts(@PathVariable("id") Long id,
                       @RequestParam(required = false) Long subcategoryId,
                       @RequestParam(required = false) Long nestedCategoryId,
                       Model model) {
        List<Product> products;
        if (nestedCategoryId !=null) {
            products = productService.getProductsByNestedCategoryId(nestedCategoryId);
        } else if (subcategoryId != null && nestedCategoryId== null) {
            products = productService.getProductsBySubCategoryId(subcategoryId);
        } else {
            products = productService.getProductsByMainCategoryId(id);
        }
        model.addAttribute("productsList", products);

        //Filters
        model.addAttribute("categoryFilters",productService.getCategoriesFilter());
        model.addAttribute("mainCategoryId", id);


        return "products";
    }
}
