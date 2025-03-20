package mainpackage.interstore.repository;

import mainpackage.interstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsByNestedCategoryId(Long id);
    Optional<Product> findById(Long id);
}