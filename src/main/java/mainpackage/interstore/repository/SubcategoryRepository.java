package mainpackage.interstore.repository;

import mainpackage.interstore.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findAllByMainCategoryId(long id);
    Optional<Subcategory> findByName(String name);
}
