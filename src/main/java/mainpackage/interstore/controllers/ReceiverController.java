package mainpackage.interstore.controllers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Dimensions;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Tag;
import mainpackage.interstore.model.util.ProductReceiverDTO;
import mainpackage.interstore.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
@Slf4j
@RestController
@RequestMapping("/receiver")
public class ReceiverController {
    private final ProductService productService;
    @Autowired
    public ReceiverController(ProductService productService) {
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

        // 1. Сохраняем изображения на диск / в облако
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile file : images) {
            String fileName = file.getOriginalFilename();
            String filePath = "src/main/resources/static/product_images/" + fileName;
            file.transferTo(Path.of(filePath)); // Теперь путь включает имя файла
            imagePaths.add(fileName); // путь для сохранения в БД
        }

        // 2. Заполняем productReceiverDTO путями
        productReceiverDTO.setProductImages(imagePaths);

        // 3. Обрабатываем DTO как обычно
        productService.handleReceivedProduct(productReceiverDTO);

        return ResponseEntity.ok("Product saved successfully");
    }





}
