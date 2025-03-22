package mainpackage.interstore.repository;

import mainpackage.interstore.model.Dimensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Repository
public interface DimensionsRepository extends JpaRepository<Dimensions,Long> {
    Dimensions findDimensionsById(Long id);
    List<Dimensions> findBySizeIn(Set<String> dimensionNames);
}
