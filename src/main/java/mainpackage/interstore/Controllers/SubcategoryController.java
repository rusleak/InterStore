package mainpackage.interstore.Controllers;

import mainpackage.interstore.Entities.Category;
import mainpackage.interstore.Entities.Product;
import mainpackage.interstore.Entities.Subcategory;
import mainpackage.interstore.Services.ProductService;
import mainpackage.interstore.Services.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class SubcategoryController {
    private final SubcategoryService subcategoryService;
    private final ProductService productService;

    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService, ProductService productService) {
        this.subcategoryService = subcategoryService;
        this.productService = productService;
    }

    @GetMapping("/subcategory/{id}")
    public String getProductsFromSpecifiedSubcategory(
            @PathVariable Long id,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            Model model) {

        Optional<Subcategory> currentSubcategoryOpt = subcategoryService.findById(id);
        if (currentSubcategoryOpt.isPresent()) {
            Subcategory currentSubcategory = currentSubcategoryOpt.get();
            Long categoryId = currentSubcategory.getCategory().getId();
            List<Subcategory> subcategories = subcategoryService.getSubcategoriesByCategoryId(categoryId);
            subcategories.remove(currentSubcategory);
            model.addAttribute("subcategories", subcategories);

            //Всі должен быть с id 0 в бд обящзательно
            List<Product> productList;
            if (currentSubcategory.getName().equals("Всі") || currentSubcategory.getId() == 0){
                productList = productService.findAll();
            } else {
                productList = productService.findAllBySubcategoryId(id);
            }


            // Filter products by price range if minPrice and maxPrice are provided
            if (minPrice != null && maxPrice != null) {
                List<Product> productsInPriceRange = new ArrayList<>();
                for (Product product : productList) {
                    BigDecimal price = product.getPrice();
                    Long priceLong = price.toBigInteger().longValue();
                    if (priceLong >= minPrice && priceLong <= maxPrice) {
                        productsInPriceRange.add(product);
                    }
                }
                productList = productsInPriceRange;
            }



            model.addAttribute("productList", productList);
            model.addAttribute("categoryName", currentSubcategory.getName());

            // Colors
            List<String> colors = Arrays.asList("red", "green", "blue");
            model.addAttribute("colors", colors);

            // Price ranges
            List<Long> minMaxPrice = productService.findMinAndMaxPrice(productList);
            System.out.println(minMaxPrice.toString());
            model.addAttribute("minPriceFromCategory", minMaxPrice.get(0));
            model.addAttribute("maxPriceFromCategory", minMaxPrice.get(1));
        }
        return "products";
    }
}