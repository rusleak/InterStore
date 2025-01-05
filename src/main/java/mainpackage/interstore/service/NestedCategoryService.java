package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.repository.NestedCategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NestedCategoryService {

    private final NestedCategoryRepository nestedCategoryRepository;
}
