package mainpackage.interstore.priceUtils;

import mainpackage.interstore.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PriceUtils {

    public static List<String> calculatePriceRanges(Integer minPrice, Integer maxPrice) {
        // Количество диапазонов
        int numRanges = 4;

        // Ширина диапазона
        int rangeWidth = (maxPrice - minPrice) / numRanges;

        List<String> priceRanges = new ArrayList<>();

        for (int i = 0; i < numRanges; i++) {
            int rangeMin = minPrice + (i * rangeWidth);
            // Для последнего диапазона верхняя граница будет равна maxPrice
            int rangeMax = (i == numRanges - 1) ? maxPrice : minPrice + ((i + 1) * rangeWidth);

            // Добавляем строку в формате "X-Y" без "от" и "до"
            priceRanges.add(rangeMin + "-" + rangeMax);
        }

        // Возвращаем список строк с диапазонами
        return priceRanges;
    }

}
