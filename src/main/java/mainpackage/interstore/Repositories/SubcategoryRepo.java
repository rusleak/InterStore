package mainpackage.interstore.Repositories;

import mainpackage.interstore.Entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepo extends JpaRepository<Subcategory,Long> {
    List<Subcategory> getSubcategoriesByCategory_Id(Long categoryId);
}
