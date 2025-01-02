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
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String getProductsFromSpecifiedSubcategory(@PathVariable Long id, Model model){
        Optional<Subcategory> currentSubcategoryOpt = subcategoryService.findById(id);
        Subcategory currentSubcategory = currentSubcategoryOpt.get();
        if(currentSubcategoryOpt.isPresent()){
            Long categoryId = currentSubcategory.getCategory().getId();
            List<Subcategory> subcategories = subcategoryService.getSubcategoriesByCategoryId(categoryId);
            subcategories.remove(currentSubcategory);
            model.addAttribute("subcategories", subcategories);

            //Продукты
            //Всі должно стоять с id 0 в БД, обязательно !!!!
            List<Product> productList;
            if (currentSubcategory.getName().equals("Всі") || currentSubcategory.getId() == 0){
                productList = productService.findAll();
            } else {
                productList = productService.findAllBySubcategoryId(id);
            }
            model.addAttribute("productList", productList);

            model.addAttribute("categoryName", currentSubcategory.getName());
            // Цвета
            List<String> colors = Arrays.asList("red", "green", "blue");
            model.addAttribute("colors", colors);

            // Диапазоны цен
        }


        return "products";
    }
}
