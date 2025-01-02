package mainpackage.interstore.Repositories;

import mainpackage.interstore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findAllByCategoryId(Long id);

    List<Product> findAllBySubcategoryId(Long id);
}
