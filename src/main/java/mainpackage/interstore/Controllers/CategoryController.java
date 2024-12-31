package mainpackage.interstore.Controllers;


import mainpackage.interstore.Entities.Category;
import mainpackage.interstore.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model){
        List<Category> categoriesList = categoryService.findAll();
        if(!categoriesList.isEmpty()){
           model.addAttribute("categoriesList",categoriesList);
        }
        return "categories";
    }
}
