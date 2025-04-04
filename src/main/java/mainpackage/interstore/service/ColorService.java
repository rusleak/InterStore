package mainpackage.interstore.service;

import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.DTOs.ColorDTO;
import mainpackage.interstore.model.DTOs.ColorUpdateDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.util.FileManager;
import mainpackage.interstore.repository.ColorRepository;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Service
public class ColorService {
    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;
    @Autowired
    public ColorService(ColorRepository colorRepository, ProductRepository productRepository) {
        this.colorRepository = colorRepository;
        this.productRepository = productRepository;
    }

    public Optional<Color> findColorById(Long colorId) {
        return colorRepository.findById(colorId);
    }

    public List<Color> findByNameIn(Set<String> colorNames) {
        return colorRepository.findByNameIn(colorNames);
    }

    public List<Color> loadColors(List<Long> colorIds) {
        List<Color> colors = new ArrayList<>();
        for (Long colorId : colorIds) {
            Optional<Color> optionalColor = colorRepository.findById(colorId);
            optionalColor.ifPresent(colors::add);  // Если Optional не пуст, добавляем в список
        }
        return colors;
    }
    public void validateColorDTO(ColorDTO colorDTO, MultipartFile multipartFile) throws Exception {
        if ((colorRepository.findByName(colorDTO.getName()).isPresent())) {
            throw new Exception("Color with that name already exists");
        }
        if(colorDTO.getName() == null)
            throw new Exception("Name cant be null for color");
        if(multipartFile.isEmpty()) {
            throw new Exception("Image cant be null for color");
        }
    }

    public void create(ColorDTO colorDTO, MultipartFile multipartFile) throws Exception {
        validateColorDTO(colorDTO,multipartFile);
        Color color = new Color();
        //setName
        TransformerDTO.dtoToColor(colorDTO, color);
        //setImage
        Path path = Path.of("src/main/resources/static/colors_for_products" + "/"+ multipartFile.getOriginalFilename());
        multipartFile.transferTo(path);
        color.setImageUrl(multipartFile.getOriginalFilename());
        colorRepository.save(color);
    }

    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    public Color findColorByIdController(Long colorId) throws Exception {
        Optional<Color> color = findColorById(colorId);
        if(color.isPresent()) {
            return color.get();
        } else {
         throw new Exception("Color not found, id " + colorId);
        }
    }
    public Color validateColorUpdateDTO(Long colorId, ColorUpdateDTO colorDTO, MultipartFile multipartFile) throws Exception {
        Optional<Color> optionalColor =  colorRepository.findById(colorId);
        if(optionalColor.isEmpty()) {
            throw new Exception("Color with that id not found");
        }
        if(colorDTO.getName() == null)
            throw new Exception("Name cant be null for color");
        if(multipartFile.isEmpty()) {
            throw new Exception("Image cant be null for color");
        }
        return optionalColor.get();
    }
    public void update(Long colorId, ColorUpdateDTO colorUpdateDTO, MultipartFile image) throws Exception {
        Color color = validateColorUpdateDTO(colorId, colorUpdateDTO, image);
        List<Product> productList = productRepository.findAllById(colorUpdateDTO.getProductIds());

        // Удаление старых связей
        for (Product oldProduct : color.getProducts()) {
            if (!productList.contains(oldProduct)) {
                oldProduct.getColors().remove(color);
            }
        }


        String fileName = image.getOriginalFilename();
        // Работа с файлами
        String path = "src/main/resources/static/colors_for_products";
        if(!color.getImageUrl().equals(fileName)){
            String oldFilePath = path + File.separator + color.getImageUrl();
            String newFilePath = path + File.separator + fileName;

            FileManager.deleteFile(oldFilePath);

            image.transferTo(Path.of(newFilePath));
        }


        // Обновляем данные цвета
        color.setName(colorUpdateDTO.getName());
        color.setImageUrl(fileName);

        // Обновляем связи с продуктами
        for (Product product : productList) {
            if (!product.getColors().contains(color)) {
                product.getColors().add(color);
            }
        }

        // Сохранение обновлённых данных
        productRepository.saveAll(productList);
        colorRepository.save(color);
    }

    public void delete(Long colorId) throws Exception {
        Optional<Color> optionalColor = colorRepository.findById(colorId);
        if(optionalColor.isPresent()) {
            Color color = optionalColor.get();
            if(color.getProducts().isEmpty() || color.getProducts() == null) {
                colorRepository.delete(color);
            } else {
                throw new Exception("Can't delete color with products attached to it, id" + colorId);
            }
        } else {
            throw new Exception("Can't find color with id " + colorId);
        }
    }

    public List<Color> getAvailableColors(Long id) {
        return findAll();
    }
}
