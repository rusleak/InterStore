package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

}
