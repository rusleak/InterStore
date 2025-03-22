package mainpackage.interstore.service;

import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ColorService {
    private final ColorRepository colorRepository;
    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public List<Color> getAvailableColors(List<Product> productList) {
        Set<Color> availableColors = new HashSet<>();

        for (Product product : productList) {
            availableColors.addAll(product.getColors()); // Add all colors of the product to the set (to avoid duplicates)
        }

        return new ArrayList<>(availableColors); // Return as a list
    }

    public Optional<Color> findColorById(Long colorId) {
        return colorRepository.findById(colorId);
    }

    public List<Color> findByNameIn(Set<String> colorNames) {
        return colorRepository.findByNameIn(colorNames);
    }
}
