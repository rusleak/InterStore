package mainpackage.interstore.service;

import mainpackage.interstore.model.Brand;
import mainpackage.interstore.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand loadBrand(Long brandId) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        return optionalBrand.orElse(new Brand());  // Если не найдено, возвращаем новый объект
    }
}
