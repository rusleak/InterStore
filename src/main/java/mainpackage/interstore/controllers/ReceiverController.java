package mainpackage.interstore.controllers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Dimensions;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Tag;
import mainpackage.interstore.model.util.MainCategoryDTO;
import mainpackage.interstore.model.util.ProductReceiverDTO;
import mainpackage.interstore.model.util.SubCategoryDTO;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import mainpackage.interstore.service.SubcategoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
@Slf4j
@RestController
@RequestMapping("/receiver")
public class ReceiverController {
    @Value("${pictures.mainCategory}")
    private String mainCategoryUploadDir;
    private final ProductService productService;
    private final MainCategoryService mainCategoryService;
    private final SubcategoryService subcategoryService;
    @Autowired
    public ReceiverController(ProductService productService, MainCategoryService mainCategoryService, SubcategoryService subcategoryService) {
        this.productService = productService;
        this.mainCategoryService = mainCategoryService;
        this.subcategoryService = subcategoryService;
    }
    //TODO Сделать методы для добавления всех сущностей в других контроллерах такиз как nested/main etc
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProdById(@PathVariable("productId") long prodId) {
        Product product = productService.findById(prodId);
        ProductReceiverDTO dto = new ProductReceiverDTO();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setProductImages(product.getProductImages());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setBrand(product.getBrand());
        dto.setOneCId(product.getOneCId());

        // Маппинг dimensions (List<Dimensions> -> List<String>)
        List<String> dimensionsNames = new ArrayList<>();
        if (product.getDimensions() != null) {
            for (Dimensions dim : product.getDimensions()) {
                dimensionsNames.add(dim.getSize());
            }
        }
        dto.setDimensions(dimensionsNames);

        // Маппинг nestedCategory (NestedCategory -> String)
        if (product.getNestedCategory() != null) {
            dto.setNestedCategory(product.getNestedCategory().getName());
        }

        // Маппинг colors (List<Color> -> List<String>)
        List<String> colorNames = new ArrayList<>();
        if (product.getColors() != null) {
            for (Color color : product.getColors()) {
                colorNames.add(color.getName());
            }
        }
        dto.setColors(colorNames);

        // Маппинг tags (List<Tag> -> List<String>)
        List<String> tagNames = new ArrayList<>();
        if (product.getTagList() != null) {
            for (Tag tag : product.getTagList()) {
                tagNames.add(tag.getName());
            }
        }
        dto.setTagList(tagNames);

        return ResponseEntity.ok(dto);
    }
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleProduct(
            @RequestPart("product") ProductReceiverDTO productReceiverDTO,
            @RequestPart("images") List<MultipartFile> images
    ) throws Exception {
        productService.processProduct(productReceiverDTO, images, false);
        return ResponseEntity.ok("Product saved successfully");
    }

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleProducts(
            @RequestPart("products") List<ProductReceiverDTO> productReceiverDTOs,
            @RequestPart("images") List<MultipartFile> images
    ) throws Exception {
        for (ProductReceiverDTO productReceiverDTO : productReceiverDTOs){
            productService.processProduct(productReceiverDTO, images, true);
        }
        return ResponseEntity.ok("Product saved successfully");
    }

    @PostMapping("/main-category")
    public ResponseEntity<?> receiveMainCategory(@RequestPart("category") MainCategoryDTO mainCategoryDTO,
                                                 @RequestPart("image") MultipartFile multipartFile) throws IOException {
        mainCategoryService.receiveMainCategory(mainCategoryDTO,multipartFile,mainCategoryUploadDir);
        return ResponseEntity.ok("Main category received successfully");
    }

    @PostMapping("/sub-category")
    public ResponseEntity<?> receiveSubCategory(@RequestBody SubCategoryDTO subCategoryDTO){
        subcategoryService.receiveSubCategory(subCategoryDTO);
        return ResponseEntity.ok("Subcategory received successfully");
    }








}
