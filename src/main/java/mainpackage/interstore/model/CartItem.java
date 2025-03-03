package mainpackage.interstore.model;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CartItem {
    private Product product;
    private int quantity;
    private Color selectedColor; // Выбранный цвет
    private Dimensions selectedSize; // Выбранный размер


    public CartItem(Product product, int quantity, Color selectedColor, Dimensions selectedSize) {
        this.product = product;
        this.quantity = quantity;
        this.selectedColor = selectedColor;
        this.selectedSize = selectedSize;
    }


    public BigDecimal getTotalPrice() {
        if (product.getDiscountedPrice() != null) {
            return product.getDiscountedPrice().multiply(BigDecimal.valueOf(quantity));
        } else {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Dimensions getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(Dimensions selectedSize) {
        this.selectedSize = selectedSize;
    }
}



