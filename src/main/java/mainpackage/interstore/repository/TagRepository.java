package mainpackage.interstore.repository;

import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByNameIn(Set<String> tagNames);
    @Query("SELECT DISTINCT t FROM Tag t " +
            "JOIN t.products p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "WHERE s.id = :subcategoryId")
    List<Tag> findAvailableTagsBySubcategory(@Param("subcategoryId") Long subcategoryId);

    @Query("SELECT DISTINCT t FROM Tag t " +
            "JOIN t.products p " +
            "JOIN p.nestedCategory nc " +
            "JOIN nc.subcategory s " +
            "JOIN s.mainCategory m " +
            "WHERE m.id = :mainCategoryId")
    List<Tag> findAvailableTagsByMainCategory(@Param("mainCategoryId") Long mainCategoryId);
    Optional<Tag> findByName(String name);
}
