package mainpackage.interstore.controllers.REST;

import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.model.DTOs.SubCategoryDTO;
import mainpackage.interstore.model.DTOs.SubCategoryUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/sub-category")
public class SubCategoryRestController {
    private final SubcategoryService subcategoryService;
    @Autowired
    public SubCategoryRestController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> receiveSubCategory(@RequestBody SubCategoryDTO subCategoryDTO){
        subcategoryService.create(subCategoryDTO);
        return ResponseEntity.ok("Subcategory received successfully");
    }
    //READ
    @GetMapping("/all")
    public ResponseEntity<?> getAllSubCat(){
        return ResponseEntity.ok(TransformerDTO.listOfSubCatToDTO(subcategoryService.findAll()));
    }

    @GetMapping("/{subCatId}")
    public ResponseEntity<?> getSubCatById(@PathVariable("subCatId") Long subCatId){
        Optional<Subcategory> subcategory= Optional.ofNullable(subcategoryService.subCategoryIsNotFound(subCatId));
        return ResponseEntity.ok(TransformerDTO.subCategoryToDto(subcategory.get()));
    }

    @GetMapping("by-main-cat/{mainCategoryId}")
    public ResponseEntity<?> getAllSubCatByMainCat(@PathVariable("mainCategoryId") Long mainCategoryId){
        return ResponseEntity.ok(TransformerDTO.listOfSubCatToDTO(subcategoryService.findAllByMainCategoryId(mainCategoryId)));
    }
    //UPDATE
    @PutMapping("/{subCatId}")
    public ResponseEntity<?> updateSubCategoryById(@PathVariable("subCatId") Long subCatId, @RequestBody SubCategoryUpdateDTO subCategoryUpdateDTO){
        subcategoryService.update(subCatId,subCategoryUpdateDTO);
        return ResponseEntity.ok("Updated successfully");
    }
    //DELETE
    @DeleteMapping("/{subCatId}")
    public ResponseEntity<?> deleteSubCatById(@PathVariable("subCatId") Long subCatId){
        subcategoryService.delete(subCatId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
