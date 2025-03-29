package mainpackage.interstore.controllers.REST;
import lombok.extern.slf4j.Slf4j;
import mainpackage.interstore.model.*;
        import mainpackage.interstore.model.DTOs.ProductDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;


    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    //CREATE
    //не обязательная передача imageUrl в body , но имя файла обязательно уникальное имя
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestPart("product") ProductDTO productDTO,
                                           @RequestPart("images") List<MultipartFile> images) throws Exception {
        productService.createProduct(productDTO, images);
        return ResponseEntity.ok("Product created successfully");
    }

    //READ
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProdById(@PathVariable("productId") long prodId) {
        return ResponseEntity.ok(TransformerDTO.productToDTO(productService.findById(prodId)));
    }
    @GetMapping("/nested/{nestedCategoryId}")
    public ResponseEntity<?> getProdByNestedCat(@PathVariable("nestedCategoryId") long nestedCategoryId) {
        List<Product> productList = productService.getProductsByNestedCategoryId(nestedCategoryId);
        return ResponseEntity.ok(TransformerDTO.listOfProductToDTO(productList));
    }
    @GetMapping("/sub/{subCatId}")
    public ResponseEntity<?> getProdBySubCat(@PathVariable("subCatId") long subCatId) {
        List<Product> productList = productService.getProductsBySubCategoryId(subCatId);
        return ResponseEntity.ok(TransformerDTO.listOfProductToDTO(productList));
    }

    @GetMapping("/main/{mainCatId}")
    public ResponseEntity<?> getProdByMainCat(@PathVariable("mainCatId") long mainCatId) {
        List<Product> productList = productService.getProductsByMainCategoryId(mainCatId);
        return ResponseEntity.ok(TransformerDTO.listOfProductToDTO(productList));
    }

    //UPDATE
    //Поиск по 1C id переданному в Body
    //Обязательная передача imageUrl в body обязательно уникальное имя
    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestPart("product") ProductDTO productDTO,
                                           @RequestPart("images") List<MultipartFile> images) throws Exception {
        productService.updateProduct(productDTO, images);
        return ResponseEntity.ok("Product updated successfully");
    }

    @PutMapping("/{status}")
    public ResponseEntity<?> updateIsActiveStatus(@RequestBody List<Long> productsIds, @PathVariable Integer status) {
        productService.updateIsActiveStatus(productsIds, status);
        return ResponseEntity.ok("Status was successfully changed");
    }
}

