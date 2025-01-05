package mainpackage.interstore.repository;

import mainpackage.interstore.model.NestedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NestedCategoryRepository extends JpaRepository<NestedCategory, Long> {
}
