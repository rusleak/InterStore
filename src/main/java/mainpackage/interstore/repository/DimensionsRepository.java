package mainpackage.interstore.repository;

import mainpackage.interstore.model.Dimensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionsRepository extends JpaRepository<Dimensions,Long> {
    Dimensions findDimensionsById(Long id);
}
