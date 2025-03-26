//package mainpackage.interstore.controllers.REST;
//import lombok.extern.slf4j.Slf4j;
//import mainpackage.interstore.model.*;
//import mainpackage.interstore.model.DTOs.ProductReceiverDTO;
//import mainpackage.interstore.model.DTOs.TransformerDTO;
//import mainpackage.interstore.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/receiver")
//public class ProductRestController {
//
//    private final ProductService productService;
//
//
//    @Autowired
//    public ProductRestController(ProductService productService) {
//        this.productService = productService;
//    }


//    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> handleProduct(
//            @RequestPart("product") ProductReceiverDTO productReceiverDTO,
//            @RequestPart("images") List<MultipartFile> images
//    ) throws Exception {
//        productService.processProduct(productReceiverDTO, images, false);
//        return ResponseEntity.ok("Product saved successfully");
//    }
//
//    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> handleProducts(
//            @RequestPart("products") List<ProductReceiverDTO> productReceiverDTOs,
//            @RequestPart("images") List<MultipartFile> images
//    ) throws Exception {
//        for (ProductReceiverDTO productReceiverDTO : productReceiverDTOs){
//            productService.processProduct(productReceiverDTO, images, true);
//        }
//        return ResponseEntity.ok("Product saved successfully");
//    }
//}
//}


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

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestPart("product") ProductDTO productDTO,
                                           @RequestPart("images") List<MultipartFile> images) throws Exception {
        Product product = new Product();
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
    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestPart("product") ProductDTO productDTO,
                                           @RequestPart("images") List<MultipartFile> images) throws Exception {
        productService.updateProduct(productDTO, images);
        return ResponseEntity.ok("Product created successfully");
    }
}

