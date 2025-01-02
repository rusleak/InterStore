package mainpackage.interstore.Controllers;

import mainpackage.interstore.Entities.Category;
import mainpackage.interstore.Entities.Product;
import mainpackage.interstore.Entities.Subcategory;
import mainpackage.interstore.Services.CategoryService;
import mainpackage.interstore.Services.ProductService;
import mainpackage.interstore.Services.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, SubcategoryService subcategoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
    }


    @GetMapping("category/{id}")
        public String getAllProductsFromCategory(@PathVariable Long id, Model model) {


        List<Product> productsList = productService.findAllByCategoryId(id);
        model.addAttribute("productList",productsList);

        if (!productsList.isEmpty()){
            String categoryName = productsList.get(0).getCategory().getName();
            model.addAttribute("categoryName",categoryName);
        } else {
            String categoryName = "В цій категорії немає товарів";
            model.addAttribute("categoryName",categoryName);
        }

        List<Subcategory> subcategories = subcategoryService.getSubcategoriesByCategoryId(id);
        model.addAttribute("subcategories", subcategories);


        return "products";
        }
}
