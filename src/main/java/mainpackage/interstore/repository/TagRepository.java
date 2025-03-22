package mainpackage.interstore.repository;

import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByNameIn(Set<String> tagNames);
}
