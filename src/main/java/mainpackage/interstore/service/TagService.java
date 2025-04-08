package mainpackage.interstore.service;

import mainpackage.interstore.model.Brand;
import mainpackage.interstore.model.DTOs.BrandUpdateDTO;
import mainpackage.interstore.model.DTOs.TagDTO;
import mainpackage.interstore.model.DTOs.TagUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Tag;
import mainpackage.interstore.repository.ProductRepository;
import mainpackage.interstore.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final ProductRepository productRepository;
    @Autowired
    public TagService(TagRepository tagRepository, ProductRepository productRepository) {
        this.tagRepository = tagRepository;
        this.productRepository = productRepository;
    }

    public List<Tag> findByNameIn(Set<String> tagNames) {
        return tagRepository.findByNameIn(tagNames);
    }

    public Optional<Tag> findById(Long tagId) {
        return tagRepository.findById(tagId);
    }

    public List<Tag> loadTags(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Optional<Tag> optionalTag = tagRepository.findById(tagId);
            optionalTag.ifPresent(tags::add);
        }
        return tags;
    }

    public void create(TagDTO tagDTO) throws Exception {
        validateTagDTO(tagDTO);
        Tag tag = new Tag();
        TransformerDTO.dtoToTag(tagDTO, tag);
        tagRepository.save(tag);
    }
    public void validateTagDTO(TagDTO tagDTO) throws Exception {
        if ((tagRepository.findByName(tagDTO.getName()).isPresent())) {
            throw new Exception("Tag with that name already exists");
        }
        if(tagDTO.getName() == null)
            throw new Exception("Name cant be null for tag");
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public TagDTO findByIdController(Long tagId) throws Exception {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if(optionalTag.isPresent()) {
            return TransformerDTO.tagToDTO(optionalTag.get());
        } else {
            throw new Exception("Tag with this id not found");
        }
    }

    public Tag validateTagUpdateDTO(TagUpdateDTO tagUpdateDTO, Long tagId) throws Exception {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if(optionalTag.isEmpty())
            throw new Exception("Can't find tag with id: " + tagId);
        if (tagRepository.findByName(tagUpdateDTO.getName()).isPresent()) {
            throw new Exception("Tag with that name already exists");
        }
        if(tagUpdateDTO.getName() == null)
            throw new Exception("Name cant be null for tag");
        return optionalTag.get();
    }
    public void update(Long tagId, TagUpdateDTO tagUpdateDTO) throws Exception {
        Tag tag = validateTagUpdateDTO(tagUpdateDTO, tagId);
        List<Product> productList = productRepository.findAllById(tagUpdateDTO.getProductIds());

        // 1. Удаляем старые связи с продуктами, которые больше не должны иметь этот тег
        for (Product oldProduct : tag.getProducts()) {
            if (!productList.contains(oldProduct)) {
                oldProduct.getTagList().remove(tag); // Удаляем старую связь
            }
        }

        // 2. Обновляем название тега
        tag.setName(tagUpdateDTO.getName());

        // 3. Добавляем новый тег только к тем продуктам, которых нет в списке
        for (Product product : productList) {
            if (!product.getTagList().contains(tag)) {
                product.getTagList().add(tag); // Добавляем новый тег
            }
        }

        // 4. Сохраняем изменения
        productRepository.saveAll(productList); // Сохраняем продукты с обновленными тегами
        tagRepository.save(tag); // Сохраняем обновленный тег
    }

    public void delete(Long tagId) throws Exception {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if(optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if(tag.getProducts().isEmpty() || tag.getProducts() == null) {
                tagRepository.delete(tag);
            } else {
                throw new Exception("Can't delete tag with products attached to it, id" + tagId);
            }
        } else {
            throw new Exception("Can't find tag with id " + tagId);
        }
    }
    public List<Tag> findAvailableTagsByMainCategory(Long mainCategoryId) {
        return tagRepository.findAvailableTagsByMainCategory(mainCategoryId);
    }
    public List<Tag> findAvailableTagsBySubcategory(Long subcategoryId) {
        return tagRepository.findAvailableTagsBySubcategory(subcategoryId);
    }
}
