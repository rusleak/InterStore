package mainpackage.interstore.controllers.REST;

import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.DTOs.ColorDTO;
import mainpackage.interstore.model.DTOs.ColorUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.ColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/color")
public class ColorRestController {
    private final ColorService colorService;

    public ColorRestController(ColorService colorService) {
        this.colorService = colorService;
    }
    //CREATE
    @PostMapping
    public ResponseEntity<?> createColor(@RequestPart ColorDTO color,
                                         @RequestPart MultipartFile image) throws Exception {
        colorService.create(color,image);
        return ResponseEntity.ok("Color created successfully");
    }
    //READ
    @GetMapping("/all")
    public ResponseEntity<?> getAllColors() {
        return ResponseEntity.ok(TransformerDTO.listOfColorsToDTO(colorService.findAll()));
    }
    @GetMapping("/{colorId}")
    public ResponseEntity<?> getColorById(@PathVariable Long colorId) throws Exception {
        return ResponseEntity.ok(TransformerDTO.colorToDTO(colorService.findColorByIdController(colorId)));
    }
    //UPDATE
    @PutMapping("/{colorId}")
    public ResponseEntity<?> updateColor(@PathVariable Long colorId,
                                         @RequestPart ColorUpdateDTO colorUpdateDTO,
                                         @RequestPart MultipartFile image) throws Exception {
        colorService.update(colorId,colorUpdateDTO,image);
        return ResponseEntity.ok("Color updated successfully");
    }
    //DELETE
    @DeleteMapping("/{colorId}")
    public ResponseEntity<?> deleteColorById(@PathVariable Long colorId) throws Exception {
        colorService.delete(colorId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
