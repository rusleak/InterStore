package mainpackage.interstore.service;

import mainpackage.interstore.model.Dimensions;
import mainpackage.interstore.repository.DimensionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DimensionsService {
    private final DimensionsRepository dimensionsRepository;
    @Autowired
    public DimensionsService(DimensionsRepository dimensionsRepository) {
        this.dimensionsRepository = dimensionsRepository;
    }

    public Optional<Dimensions> findDimensionById(Long id) {
        return dimensionsRepository.findById(id);
    }
}
