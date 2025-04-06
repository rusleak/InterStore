package mainpackage.interstore.model.util;

import java.math.BigDecimal;

public interface PriceRange {
    BigDecimal getMinPrice();
    BigDecimal getMaxPrice();
}
