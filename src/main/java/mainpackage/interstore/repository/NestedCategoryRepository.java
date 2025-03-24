package mainpackage.interstore.repository;

import mainpackage.interstore.model.NestedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NestedCategoryRepository extends JpaRepository<NestedCategory, Long> {
    Optional<NestedCategory> findById(Long id);
    Optional<NestedCategory> findByName(String nestedCategoryName);
    List<NestedCategory> findAllBySubcategoryId(Long subCatId);
}
