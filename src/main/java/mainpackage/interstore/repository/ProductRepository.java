package mainpackage.interstore.repository;

import mainpackage.interstore.model.Product;
import mainpackage.interstore.model.Subcategory;
import mainpackage.interstore.model.util.PriceRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsByNestedCategoryId(Long id);
    Optional<Product> findById(Long id);
    Optional<Product> findByOneCId(Long oneC_id);
    Page<Product> findByNestedCategory_Subcategory_MainCategory_Id(Long mainCategoryId, Pageable pageable);

    Page<Product> findByNestedCategory_Subcategory_Id(Long subcategoryId, Pageable pageable);

    Page<Product> findByNestedCategoryId(Long nestedCategoryId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.nestedCategory nc " +
            "LEFT JOIN nc.subcategory sc " +
            "LEFT JOIN p.colors col " +
            "LEFT JOIN p.dimensions d " +
            "LEFT JOIN p.tagList t " +
            "LEFT JOIN p.brand b " +
            "WHERE (:nestedCategoryId IS NULL OR nc.id = :nestedCategoryId) " +
            "AND (:subcategoryId IS NULL OR sc.id = :subcategoryId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:colors IS NULL OR col.id IN :colors) " +
            "AND (:dimensions IS NULL OR d.size IN :dimensions) " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (:brands IS NULL OR b.name IN :brands) " +
            "AND p.isActive = 1")
    Page<Product> findFilteredProducts(
            @Param("nestedCategoryId") Long nestedCategoryId,
            @Param("subcategoryId") Long subcategoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("colors") List<Long> colors,
            @Param("dimensions") List<String> dimensions,
            @Param("tags") List<String> tags,
            @Param("brands") List<String> brands,
            Pageable pageable);

    //PRICE RANGE
    @Query(value = "SELECT FLOOR(MIN(p.price)) - 1 AS minPrice, CEILING(MAX(p.price)) + 1 AS maxPrice " +
            "FROM products p " +
            "JOIN nested_categories nc ON p.nested_category_id = nc.id " +
            "JOIN subcategories s ON nc.subcategory_id = s.id " + // исправлено имя таблицы subcategory
            "WHERE s.id = :subCategoryId", nativeQuery = true)
    PriceRange findPriceRangeBySubCategory(@Param("subCategoryId") Long subCategoryId);


    @Query(value = "SELECT FLOOR(MIN(p.price)) - 1 AS minPrice, CEILING(MAX(p.price)) + 1 AS maxPrice " +
            "FROM products p " +
            "JOIN nested_categories nc ON p.nested_category_id = nc.id " +
            "JOIN subcategories s ON nc.subcategory_id = s.id " + // исправлено имя таблицы subcategory
            "JOIN main_categories m ON s.main_category_id = m.id " + // исправлено имя таблицы main_category
            "WHERE m.id = :mainCategoryId", nativeQuery = true)
    PriceRange findPriceRangeByMainCategory(@Param("mainCategoryId") Long mainCategoryId);

}