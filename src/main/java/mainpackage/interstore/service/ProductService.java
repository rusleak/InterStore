package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
