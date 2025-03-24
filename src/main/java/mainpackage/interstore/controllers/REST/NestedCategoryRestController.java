package mainpackage.interstore.controllers.REST;

import mainpackage.interstore.model.NestedCategory;
import mainpackage.interstore.model.DTOs.NestedCategoryDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.NestedCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nested-category")
public class NestedCategoryRestController {
    private final NestedCategoryService nestedCategoryService;
    @Autowired
    public NestedCategoryRestController(NestedCategoryService nestedCategoryService) {
        this.nestedCategoryService = nestedCategoryService;
    }
    //CREATE
    @PostMapping
    public ResponseEntity<?> receiveNestedCategory(@RequestBody NestedCategoryDTO nestedCategoryDTO){
        nestedCategoryService.create(nestedCategoryDTO);
        return ResponseEntity.ok("Nested-category created successfully");
    }
    //READ
    @GetMapping("/by-sub-cat/{subCatId}")
    public ResponseEntity<?> getAllBySubCat(@PathVariable Long subCatId){
        List<NestedCategory> nestedCategoryList = nestedCategoryService.findAllBySubCatId(subCatId);
        return ResponseEntity.ok(TransformerDTO.listOfNestedCatToDTO(nestedCategoryList));
    }
    @GetMapping("/{nestedCatId}")
    public ResponseEntity<?> getSubCatById(@PathVariable Long nestedCatId){
        NestedCategory nestedCategory = nestedCategoryService.nestedCategoryIsNotFound(nestedCatId);
        return ResponseEntity.ok(TransformerDTO.nestedCategoryToDTO(nestedCategory));
    }
    //UPDATE
    @PutMapping("/{nestedCatId}")
    public ResponseEntity<?> updateNestedCat(@PathVariable Long nestedCatId,@RequestBody NestedCategoryDTO nestedCategoryDTO){
        nestedCategoryService.update(nestedCatId, nestedCategoryDTO);
        return ResponseEntity.ok("Successfully updated");
    }

    @DeleteMapping("/{nestedCatId}")
    public ResponseEntity<?> deleteNestedCat(@PathVariable Long nestedCatId) {
        nestedCategoryService.delete(nestedCatId);
        return ResponseEntity.ok("Nested category was deleted successfully");
    }

}
