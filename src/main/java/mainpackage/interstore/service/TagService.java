package mainpackage.interstore.service;

import mainpackage.interstore.model.Tag;
import mainpackage.interstore.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public Optional<Tag> findById(Long tagId) {
        return tagRepository.findById(tagId);
    }

    public List<Tag> loadTags(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Optional<Tag> optionalTag = tagRepository.findById(tagId);
            optionalTag.ifPresent(tags::add);
        }
        return tags;
    }
}
