package mainpackage.interstore.controllers;

import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.service.MainCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/homepage")
public class MainCategoryController {
    private final MainCategoryService mainCategoryService;
    @Autowired
    public MainCategoryController(MainCategoryService mainCategoryService) {
        this.mainCategoryService = mainCategoryService;
    }

    @GetMapping()
    String getHomePage(Model model) {
        List<MainCategory> mainCategoryList = mainCategoryService.findAll();
        model.addAttribute(mainCategoryList);



        return "homepage";
    }
}
