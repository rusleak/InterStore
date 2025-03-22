package mainpackage.interstore.service;

import mainpackage.interstore.model.Tag;
import mainpackage.interstore.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagService {
    private final TagRepository tagRepository;
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findByNameIn(Set<String> tagNames) {
        return tagRepository.findByNameIn(tagNames);
    }
}
