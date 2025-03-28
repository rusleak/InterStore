package mainpackage.interstore.controllers.REST;

import mainpackage.interstore.model.DTOs.SubCategoryUpdateDTO;
import mainpackage.interstore.model.DTOs.TagDTO;
import mainpackage.interstore.model.DTOs.TagUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> updateTag(@RequestBody TagDTO tagDTO) throws Exception {
        tagService.create(tagDTO);
        return ResponseEntity.ok("Tag created successfully");
    }
    //READ
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(TransformerDTO.listOfTagToDTO(tagService.findAll()));
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<?> getTagById(@PathVariable Long tagId) throws Exception {
        return ResponseEntity.ok(tagService.findByIdController(tagId));
    }
    //UPDATE
    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable("tagId") Long tagId, @RequestBody TagUpdateDTO tagUpdateDTO) throws Exception {
        tagService.update(tagId,tagUpdateDTO);
        return ResponseEntity.ok("Updated successfully");
    }
    //DELETE
    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTagById(@PathVariable Long tagId) throws Exception {
        tagService.delete(tagId);
        return ResponseEntity.ok("Tag deleted, id " + tagId);
    }
}
