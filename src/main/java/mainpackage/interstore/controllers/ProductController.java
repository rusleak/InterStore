package mainpackage.interstore.controllers;

import mainpackage.interstore.model.*;
import mainpackage.interstore.model.DTOs.ProductFilterDTO;
import mainpackage.interstore.model.util.PriceRange;
import mainpackage.interstore.repository.ProductRepository;
import mainpackage.interstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("")
public class ProductController {
    private final MainCategoryService mainCategoryService;
    private final BrandService brandService;
    private final DimensionsService dimensionsService;
    private final TagService tagService;
    private final ProductService productService;
    private final ColorService colorService;
    private final SubcategoryService subcategoryService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(MainCategoryService mainCategoryService, BrandService brandService, DimensionsService dimensionsService, TagService tagService, ProductService productService, ColorService colorService, SubcategoryService subcategoryService,
                             ProductRepository productRepository) {
        this.mainCategoryService = mainCategoryService;
        this.brandService = brandService;
        this.dimensionsService = dimensionsService;
        this.tagService = tagService;
        this.productService = productService;
        this.colorService = colorService;
        this.subcategoryService = subcategoryService;
        this.productRepository = productRepository;
    }

    @GetMapping("/main-category/{id}")
    public String getProducts(
            @PathVariable("id") Long id,
            @ModelAttribute ProductFilterDTO filterDTO,
            Model model) {
        // Здесь внутри можно установить значения по умолчанию, если необходимо
        if (filterDTO.getSize() <= 0) {
            filterDTO.setSize(16);
        }
        // Остальная логика, например:
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize());
        model.addAttribute("size",filterDTO.getSize());
        model.addAttribute("mainCategoryId", id);
        model.addAttribute("subcategoryId", filterDTO.getSubcategoryId());
        model.addAttribute("nestedCategoryId", filterDTO.getNestedCategoryId());

        Page<Product> pageOfProducts = productService.getFilteredProducts(filterDTO, pageable);
        List<Product> products = pageOfProducts.getContent();
        products = productService.excludeNullPictures(products);
        products = productService.excludeNotActiveProducts(products);

        model.addAttribute("productsList", products);
        model.addAttribute("currentPage", filterDTO.getPage());
        model.addAttribute("totalPages", pageOfProducts.getTotalPages());

        PriceRange priceRange = fillModelIncludeFilters(model, filterDTO.getSubcategoryId(), id);
        model.addAttribute("selectedColors", filterDTO.getColors());
        model.addAttribute("selectedBrands", filterDTO.getBrands());
        model.addAttribute("selectedTags", filterDTO.getTags());
        model.addAttribute("selectedDimensions", filterDTO.getDimensions());

        model.addAttribute("filterMinPrice", filterDTO.getFilterMinPrice());
        model.addAttribute("filterMaxPrice", filterDTO.getFilterMaxPrice());
        model.addAttribute("placeholderFromPrice", priceRange.getMinPrice());
        model.addAttribute("placeholderToPrice", priceRange.getMaxPrice());

        model.addAttribute("categoryFilters", subcategoryService.getCategoriesFilter(id));
        model.addAttribute("activeCategory", mainCategoryService.getActiveCategory(id, filterDTO.getSubcategoryId(), filterDTO.getNestedCategoryId(), products));

        return "products";
    }


    @GetMapping("product/{id}")
    public String getProduct(@PathVariable("id") Long id,
                             Model model) {
        productService.fillTheModelProductPage(model, id);
        return "productPage";
    }

    private PriceRange fillModelIncludeFilters(Model model, Long subcategoryId, Long mainCatId){
        PriceRange priceRange = null;
        if (subcategoryId != null) {
            priceRange = productRepository.findPriceRangeBySubCategory(subcategoryId);
            model.addAttribute("availableColors", colorService.getColorsBySubcategory(subcategoryId));
            model.addAttribute("availableTags", tagService.findAvailableTagsBySubcategory(subcategoryId));
            model.addAttribute("availableDimensions", dimensionsService.findAvailableDimensionsBySubcategory(subcategoryId));
            model.addAttribute("availableBrands", brandService.getBrandNames(brandService.findAvailableBrandsBySubcategory(subcategoryId)));
        } else {
            priceRange = productRepository.findPriceRangeByMainCategory(mainCatId);
            model.addAttribute("availableTags", tagService.findAvailableTagsByMainCategory(mainCatId));
            model.addAttribute("availableDimensions", dimensionsService.findAvailableDimensionsByMainCategory(mainCatId));
            model.addAttribute("availableColors", colorService.getAvailableColorsByMainCatId(mainCatId));
            model.addAttribute("availableBrands", brandService.getBrandNames(brandService.findAvailableBrandsByMainCategory(mainCatId)));
        }
        return priceRange;
    }
}