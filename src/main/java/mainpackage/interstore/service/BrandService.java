package mainpackage.interstore.service;

import mainpackage.interstore.model.Brand;
import mainpackage.interstore.model.DTOs.BrandDTO;
import mainpackage.interstore.model.DTOs.BrandUpdateDTO;
import mainpackage.interstore.model.DTOs.TagDTO;
import mainpackage.interstore.model.DTOs.TransformerDTO;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Tag;
import mainpackage.interstore.repository.BrandRepository;
import mainpackage.interstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    @Autowired
    public BrandService(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand loadBrand(Long brandId) throws Exception {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        if(optionalBrand.isPresent()){
            return optionalBrand.get();  // Если не найдено, возвращаем новый объект
        } else {
         throw new Exception("Brand not found");
        }
    }

    public void validateBrandDTO(BrandDTO brandDTO) throws Exception {
        if (brandRepository.findByName(brandDTO.getName()).isPresent()) {
            throw new Exception("Brand with that name already exists");
        }
        if(brandDTO.getName() == null)
            throw new Exception("Name cant be null for brand");
    }
    public Brand validateBrandUpdateDTO(BrandUpdateDTO brandDTO,Long brandId) throws Exception {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        if(optionalBrand.isEmpty())
            throw new Exception("Can't find brand with id: " + brandId);
        if (brandRepository.findByName(brandDTO.getName()).isPresent()) {
            throw new Exception("Brand with that name already exists");
        }
        if(brandDTO.getName() == null)
            throw new Exception("Name cant be null for brand");
        return optionalBrand.get();
    }
    public void create(BrandDTO brandDTO) throws Exception {
        validateBrandDTO(brandDTO);
        Brand brand = new Brand();
        TransformerDTO.dtoToBrand(brandDTO, brand);
        brandRepository.save(brand);
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public void update(BrandUpdateDTO brandUpdateDTO,Long brandId) throws Exception {
        Brand brand = validateBrandUpdateDTO(brandUpdateDTO,brandId);
        List<Product> productList = productRepository.findAllById(brandUpdateDTO.getProductIds());


        brand.setName(brandUpdateDTO.getName());
        brand.setProductList(productList);

        for (Product product : productList) {
            product.setBrand(brand);
        }

        brandRepository.save(brand);
        productRepository.saveAll(productList);
    }

    public void delete(Long brandId) throws Exception {
        Optional<Brand> optBrand = findById(brandId);
        if(optBrand.isPresent()) {
            boolean isDeletable = validateBeforeDeleting(optBrand.get());
            if(isDeletable) {
                brandRepository.delete(optBrand.get());
            } else {
                throw new Exception("Can't delete brand while it has attached products");
            }
        } else {
            throw new Exception("Can't find brand with id: " + brandId);
        }
    }
    public boolean validateBeforeDeleting(Brand brand) {
        if (brand.getProductList().isEmpty() || brand.getProductList() == null) {
            return true;
        } else {
            return false;
        }
    }
    public BrandDTO findByIdController(Long brandId) throws Exception {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        if(optionalBrand.isPresent()) {
            return TransformerDTO.brandToDTO(optionalBrand.get());
        } else {
            throw new Exception("Brand with this id not found");
        }
    }
    public List<String> getBrandNames(List<Brand> brands) {
        return brands.stream()
                .map(Brand::getName)
                .collect(Collectors.toList());
    }

    public List<Brand> findAvailableBrandsBySubcategory(Long subcategoryId) {
        return brandRepository.findAvailableBrandsBySubcategory(subcategoryId);
    }
    public List<Brand> findAvailableBrandsByMainCategory(Long mainCategoryId) {
        return brandRepository.findAvailableBrandsByMainCategory(mainCategoryId);
    }
}
