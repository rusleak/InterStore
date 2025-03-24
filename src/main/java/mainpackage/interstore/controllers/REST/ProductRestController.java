package mainpackage.interstore.controllers.REST;
import lombok.extern.slf4j.Slf4j;
import mainpackage.interstore.model.*;
import mainpackage.interstore.model.DTOs.ProductReceiverDTO;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/receiver")
public class ProductRestController {

    private final ProductService productService;


    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

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
















}
