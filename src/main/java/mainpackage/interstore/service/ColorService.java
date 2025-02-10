package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorService {
    private final ColorRepository colorRepository;
    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }


}
