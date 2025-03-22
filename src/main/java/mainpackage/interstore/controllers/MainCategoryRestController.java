package mainpackage.interstore.controllers;

import mainpackage.interstore.model.util.MainCategoryDTO;
import mainpackage.interstore.service.MainCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class MainCategoryRestController {
    @Value("${pictures.mainCategory}")
    private String mainCategoryUploadDir;
    private final MainCategoryService mainCategoryService;
    @Autowired
    public MainCategoryRestController(MainCategoryService mainCategoryService) {
        this.mainCategoryService = mainCategoryService;
    }

    @PostMapping("/main-category")
    public ResponseEntity<?> receiveMainCategory(@RequestPart("category") MainCategoryDTO mainCategoryDTO,
                                                 @RequestPart("image") MultipartFile multipartFile) throws IOException {
        mainCategoryService.receiveMainCategory(mainCategoryDTO,multipartFile,mainCategoryUploadDir);
        return ResponseEntity.ok("Main category received successfully");
    }
}
