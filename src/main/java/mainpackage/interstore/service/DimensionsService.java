package mainpackage.interstore.service;

import mainpackage.interstore.model.Dimensions;
import mainpackage.interstore.repository.DimensionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DimensionsService {
    private final DimensionsRepository dimensionsRepository;
    @Autowired
    public DimensionsService(DimensionsRepository dimensionsRepository) {
        this.dimensionsRepository = dimensionsRepository;
    }

    public Optional<Dimensions> findDimensions(Long id) {
        return dimensionsRepository.findById(id);
    }

    public List<Dimensions> findByNameIn(Set<String> dimensionNames) {
        return dimensionsRepository.findBySizeIn(dimensionNames);
    }

    public List<Dimensions> loadDimensions(List<Long> dimensionsIds) {
        List<Dimensions> dimensions = new ArrayList<>();
        for (Long dimensionsId : dimensionsIds) {
            Optional<Dimensions> optionalDimensions = dimensionsRepository.findById(dimensionsId);
            optionalDimensions.ifPresent(dimensions::add);
        }
        return dimensions;
    }

}
