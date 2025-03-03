package mainpackage.interstore.controllers;

import jakarta.servlet.http.HttpSession;
import mainpackage.interstore.model.Cart;
import mainpackage.interstore.model.Color;
import mainpackage.interstore.model.Dimensions;
import mainpackage.interstore.model.Product;
import mainpackage.interstore.service.ColorService;
import mainpackage.interstore.service.DimensionsService;
import mainpackage.interstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
//TODO добавить выбор клво товаров в корзину
//TODO если нету цвета или размера то в корзину не добавляет
//TODO
@Controller
public class CartController {
    private final ProductService productService;
    private final ColorService colorService;
    private final DimensionsService dimensionsService;

    @Autowired
    public CartController(ProductService productService, ColorService colorService, DimensionsService dimensionsService) {
        this.productService = productService;
        this.colorService = colorService;
        this.dimensionsService = dimensionsService;
    }

    @PostMapping("/add")
    public String addToCart(HttpSession session,
                            @RequestParam Long productId,
                            @RequestParam Long colorId,
                            @RequestParam Long dimensionsId,
                            @RequestParam int quantity) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
            System.out.println("Создана новая корзина в сессии");
        }

        try {
            Product product = productService.findById(productId);
            Optional<Color> color = colorService.findColorById(colorId);
            Optional<Dimensions> dimensions = dimensionsService.findDimensionById(dimensionsId);

            // Проверка наличия цвета и размера перед добавлением в корзину
                cart.addProduct(product, quantity, color.get(), dimensions.get());
                System.out.println("Добавлен товар: " + product.getName() +
                        " | Цвет: " + color.get().getName() +
                        " | Размер: " + dimensions.get().getSize() +
                        " | Кол-во: " + quantity);



            session.setAttribute("cart", cart); // Обновляем корзину в сессии
            System.out.println("Товаров в корзине: " + cart.getItems().size());

        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        return "redirect:/show-cart";
    }

    @GetMapping("/show-cart")
    public String showCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        double totalAmount = 0;
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("cart", cart);
        return "cart";
    }
}