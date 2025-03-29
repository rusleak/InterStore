package mainpackage.interstore.controllers.REST;

import mainpackage.interstore.model.DTOs.BrandDTO;
import mainpackage.interstore.model.DTOs.BrandUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
public class BrandRestController {
    private final BrandService brandService;
    @Autowired
    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandDTO brandDTO) throws Exception {
        brandService.create(brandDTO);
        return ResponseEntity.ok("Brand successfully created");
    }
    //READ
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(TransformerDTO.listOfBrandToDTO(brandService.findAll()));
    }
    @GetMapping("/{brandId}")
    public ResponseEntity<?> getBrandById(@PathVariable Long brandId) throws Exception {
        return ResponseEntity.ok(brandService.findByIdController(brandId));
    }

    //UPDATE
    @PutMapping("/{brandId}")
    public ResponseEntity<?> update(@RequestBody BrandUpdateDTO brandUpdateDTO, @PathVariable Long brandId) throws Exception {
        brandService.update(brandUpdateDTO, brandId);
        return ResponseEntity.ok("Brand updated successfully");
    }
    //DELETE
    @DeleteMapping("/{brandId}")
    public ResponseEntity<?> delete(@PathVariable Long brandId) throws Exception {
        brandService.delete(brandId);
        return ResponseEntity.ok("Brand deleted successfully");
    }
}
