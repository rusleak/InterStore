package mainpackage.interstore.repository;

import mainpackage.interstore.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findByNameIn(Set<String> colorNames);

    Optional<Color> findByName(String name);
}
