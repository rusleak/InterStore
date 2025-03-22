package mainpackage.interstore.controllers;

import mainpackage.interstore.model.util.NestedCategoryDTO;
import mainpackage.interstore.service.NestedCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NestedCategoryRestController {
    private final NestedCategoryService nestedCategoryService;
    @Autowired
    public NestedCategoryRestController(NestedCategoryService nestedCategoryService) {
        this.nestedCategoryService = nestedCategoryService;
    }

    @PostMapping("/nested-category")
    public ResponseEntity<?> receiveNestedCategory(@RequestBody NestedCategoryDTO nestedCategoryDTO){
        nestedCategoryService.receiveNestedCategory(nestedCategoryDTO);
        return ResponseEntity.ok("Nested-category received successfully");
    }
}
