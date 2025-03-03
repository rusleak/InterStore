package mainpackage.interstore.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Long, CartItem> items = new HashMap<>();

    public void addProduct(Product product, int quantity, Color color, Dimensions size) {
        Long key = generateKey(product, color, size);

        if (items.containsKey(key)) {
            CartItem existingItem = items.get(key);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(key, new CartItem(product, quantity, color, size));
        }
    }

    public void removeProduct(Product product, Color color, Dimensions size) {
        Long key = generateKey(product, color, size);
        items.remove(key);
    }

    public void clearCart() {
        items.clear();
    }

    public Map<Long, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Long, CartItem> items) { // Добавляем сеттер для сериализации
        this.items = items;
    }

    private Long generateKey(Product product, Color color, Dimensions size) {
        return (product.getId() * 1000000L) + (color.getId() * 1000L) + size.getId();
    }
}
