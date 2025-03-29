package mainpackage.interstore.controllers.REST;

import mainpackage.interstore.model.MainCategory;
import mainpackage.interstore.model.DTOs.MainCategoryDTO;
import mainpackage.interstore.model.DTOs.MainCategoryUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.MainCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RelationException;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/main-category")
public class MainCategoryRestController {
    @Value("${pictures.mainCategory}")
    private String mainCategoryUploadDir;
    private final MainCategoryService mainCategoryService;
    @Autowired
    public MainCategoryRestController(MainCategoryService mainCategoryService) {
        this.mainCategoryService = mainCategoryService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> receiveMainCategory(@RequestPart("category") MainCategoryDTO mainCategoryDTO,
                                                 @RequestPart("image") MultipartFile multipartFile) throws IOException {
        MainCategory mainCategory = TransformerDTO.dtoToMainCategory(mainCategoryDTO);
        mainCategoryService.create(mainCategory,multipartFile, Path.of(mainCategoryUploadDir));
        return ResponseEntity.ok("Main category received successfully");
    }
    //READ
    @GetMapping("/all")
    public ResponseEntity<?> getAllMainCat() {
        return ResponseEntity.ok(TransformerDTO.listOfMainCatToDTO(mainCategoryService.findAll()));
    }
    //READ
    @GetMapping("/{mainCategoryId}")
    public ResponseEntity<?> getMainCatById(@PathVariable("mainCategoryId") Long mainCatId) {
        MainCategory mainCategory = mainCategoryService.getByIdForController(mainCatId);
        MainCategoryDTO mainCategoryDTO = TransformerDTO.mainCategoryToDto(mainCategory);
        return ResponseEntity.ok(mainCategoryDTO);
    }
    //UPDATE
    @PutMapping("/{mainCategoryId}")
    public ResponseEntity<?> updateMainCatById(@PathVariable("mainCategoryId") Long mainCatId,
                                               @RequestPart("category") MainCategoryUpdateDTO mainCategoryUpdateDTO,
                                               @RequestPart("image") MultipartFile multipartFile) throws IOException {
        mainCategoryService.update(mainCatId,mainCategoryUpdateDTO, multipartFile, Path.of(mainCategoryUploadDir));
        return ResponseEntity.ok("Main category with id " + mainCatId + " was successfully updated");
    }

    //DELETE
    @DeleteMapping("/{mainCategoryId}")
    public ResponseEntity<?> deleteMainCategory(@PathVariable("mainCategoryId") Long mainCatId) throws RelationException {
        mainCategoryService.delete(mainCatId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
