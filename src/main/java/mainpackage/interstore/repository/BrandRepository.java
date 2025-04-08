package mainpackage.interstore.repository;

import mainpackage.interstore.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT DISTINCT b FROM Brand b " +
            "JOIN b.productList p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "WHERE s.id = :subcategoryId")
    List<Brand> findAvailableBrandsBySubcategory(@Param("subcategoryId") Long subcategoryId);

    @Query("SELECT DISTINCT b FROM Brand b " +
            "JOIN b.productList p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "JOIN s.mainCategory m " +
            "WHERE m.id = :mainCategoryId")
    List<Brand> findAvailableBrandsByMainCategory(@Param("mainCategoryId") Long mainCategoryId);

    Optional<Brand> findByName(String name);
}
