package mainpackage.interstore.repository;

import mainpackage.interstore.model.Dimensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Repository
public interface DimensionsRepository extends JpaRepository<Dimensions,Long> {
    Dimensions findDimensionsById(Long id);
    @Query("SELECT DISTINCT d FROM Dimensions d " +
            "JOIN d.products p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "WHERE s.id = :subcategoryId")
    List<Dimensions> findAvailableDimensionsBySubcategory(@Param("subcategoryId") Long subcategoryId);

    @Query("SELECT DISTINCT d FROM Dimensions d " +
            "JOIN d.products p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "JOIN s.mainCategory m " +
            "WHERE m.id = :mainCategoryId")
    List<Dimensions> findAvailableDimensionsByMainCategory(@Param("mainCategoryId") Long mainCategoryId);

    List<Dimensions> findBySizeIn(Set<String> dimensionNames);
}
