package mainpackage.interstore.repository;

import mainpackage.interstore.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findByNameIn(Set<String> colorNames);
        @Query("SELECT DISTINCT c FROM Color c " +
                "JOIN c.products p " +
                "JOIN p.nestedCategory nc " +
                "JOIN nc.subcategory s " +
                "WHERE s.id = :subcategoryId")
        List<Color> findAvailableColorsBySubcategory(@Param("subcategoryId") Long subcategoryId);
    Optional<Color> findByName(String name);
    @Query("SELECT DISTINCT c FROM Color c " +
            "JOIN c.products p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "JOIN s.mainCategory m " +
            "WHERE m.id = :mainCategoryId")
    List<Color> findAvailableColorsByMainCategory(@Param("mainCategoryId") Long mainCategoryId);
}

