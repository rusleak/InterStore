package mainpackage.interstore.controllers;

import mainpackage.interstore.model.util.SubCategoryDTO;
import mainpackage.interstore.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubCategoryRestController {
    private final SubcategoryService subcategoryService;
    @Autowired
    public SubCategoryRestController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @PostMapping("/sub-category")
    public ResponseEntity<?> receiveSubCategory(@RequestBody SubCategoryDTO subCategoryDTO){
        subcategoryService.receiveSubCategory(subCategoryDTO);
        return ResponseEntity.ok("Subcategory received successfully");
    }
}
