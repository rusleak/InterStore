package mainpackage.interstore.controllers;

import mainpackage.interstore.model.*;
import mainpackage.interstore.model.DTOs.ProductFilterDTO;
import mainpackage.interstore.repository.ProductRepository;
import mainpackage.interstore.service.ColorService;
import mainpackage.interstore.service.MainCategoryService;
import mainpackage.interstore.service.ProductService;
import mainpackage.interstore.service.SubcategoryService;
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
    private final ProductService productService;
    private final ColorService colorService;
    private final SubcategoryService subcategoryService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(MainCategoryService mainCategoryService, ProductService productService, ColorService colorService, SubcategoryService subcategoryService,
                             ProductRepository productRepository) {
        this.mainCategoryService = mainCategoryService;
        this.productService = productService;
        this.colorService = colorService;
        this.subcategoryService = subcategoryService;
        this.productRepository = productRepository;
    }

    @GetMapping("/main-category/{id}")
    public String getProducts(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long nestedCategoryId,
            @RequestParam(required = false) String filterMinPrice,
            @RequestParam(required = false) String filterMaxPrice,
            @RequestParam(required = false) List<Long> colors,
            @RequestParam(required = false) List<String> dimensions,
            @RequestParam(required = false) List<String> tagsFromClient,
            @RequestParam(required = false) List<String> brands,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            Model model) {

        // Проверка и установка значения size по умолчанию, если необходимо
        if (size <= 0) {
            size = 16;
        }

        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("mainCategoryId", id);
        model.addAttribute("subcategoryId", subcategoryId);
        model.addAttribute("nestedCategoryId", nestedCategoryId);

        // Если нужно, можно создать объект ProductFilterDTO вручную
        ProductFilterDTO filterDTO = new ProductFilterDTO();
        filterDTO.setSubcategoryId(subcategoryId);
        filterDTO.setNestedCategoryId(nestedCategoryId);
        filterDTO.setFilterMinPrice(filterMinPrice);
        filterDTO.setFilterMaxPrice(filterMaxPrice);
        filterDTO.setColors(colors);
        filterDTO.setDimensions(dimensions);
        filterDTO.setTagsFromClient(tagsFromClient);
        filterDTO.setBrands(brands);
        filterDTO.setPage(page);
        filterDTO.setSize(size);

        // Получаем товары с учетом фильтров
        Page<Product> pageOfProducts = productService.getFilteredProducts(filterDTO, pageable);
        List<Product> products = pageOfProducts.getContent();
        products = productService.excludeNullPictures(products);

        model.addAttribute("productsList", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageOfProducts.getTotalPages());

        // Передача остальных атрибутов в модель (цвета, бренды, теги, размеры и т.д.)
        model.addAttribute("availableColors", colorService.getAvailableColors(id));
        model.addAttribute("availableBrands", productService.getAllBrandsFromProducts(products));

        if (subcategoryId != null) {
            model.addAttribute("availableTags", productService.getAllTagsFromProducts(products));
            model.addAttribute("availableDimensions", productService.getAllDimensionsFromProducts(products));
        }

        // Выбранные фильтры (если нужно, можно напрямую передавать request params)
        model.addAttribute("selectedColors", colors);
        model.addAttribute("selectedBrands", brands);
        model.addAttribute("selectedTags", tagsFromClient);
        model.addAttribute("selectedDimensions", dimensions);

        // Цена
        model.addAttribute("filterMinPrice", filterMinPrice);
        model.addAttribute("filterMaxPrice", filterMaxPrice);
        double[] priceRange = productService.getMinAndMaxPriceFromProductList(products);
        model.addAttribute("placeholderFromPrice", (int) Math.floor(priceRange[0]));
        model.addAttribute("placeholderToPrice", (int) Math.floor(priceRange[1]));

        // Категории и текущая активная
        model.addAttribute("categoryFilters", subcategoryService.getCategoriesFilter(id));
        model.addAttribute("activeCategory", mainCategoryService.getActiveCategory(id, subcategoryId, nestedCategoryId, products));

        return "products";
    }


    @GetMapping("product/{id}")
    public String getProduct(@PathVariable("id") Long id,
                             Model model) {
        productService.fillTheModelProductPage(model, id);
        return "productPage";
    }
}