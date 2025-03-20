package mainpackage.interstore.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Dimensions;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Tag;
import mainpackage.interstore.model.util.ProductReceiverDTO;
import mainpackage.interstore.service.ProductService;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        dto.setOneC_id(product.getOneC_id());

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



}
