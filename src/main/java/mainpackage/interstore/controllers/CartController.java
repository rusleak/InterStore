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

//TODO пометь отображение имени цвета над картинкой в cart
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

        Cart cart = getOrCreateCart(session);

        Optional<Product> product = Optional.ofNullable(productService.findById(productId));
        Optional<Color> color = colorService.findColorById(colorId);
        Optional<Dimensions> dimensions = dimensionsService.findDimensions(dimensionsId);

        if (product.isPresent() && color.isPresent() && dimensions.isPresent()) {
            cart.addProduct(product.get(), quantity, color.get(), dimensions.get());
        }

        session.setAttribute("cart", cart);
        return "redirect:/show-cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(HttpSession session,
                                 @RequestParam Long productId,
                                 @RequestParam Long colorId,
                                 @RequestParam Long dimensionsId,
                                 @RequestParam int quantity) {

        Cart cart = getOrCreateCart(session);

        Optional<Product> product = Optional.ofNullable(productService.findById(productId));
        Optional<Color> color = colorService.findColorById(colorId);
        Optional<Dimensions> dimensions = dimensionsService.findDimensions(dimensionsId);
        if (product.isPresent() && color.isPresent() && dimensions.isPresent()) {
            cart.updateProductQuantity(product.get(), color.get(), dimensions.get(), quantity);
        }

        session.setAttribute("cart", cart);
        return "redirect:/show-cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(HttpSession session,
                                 @RequestParam Long productId,
                                 @RequestParam Long colorId,
                                 @RequestParam Long dimensionsId) {

        Cart cart = getOrCreateCart(session);

        Optional<Product> product = Optional.ofNullable(productService.findById(productId));
        Optional<Color> color = colorService.findColorById(colorId);
        Optional<Dimensions> dimensions = dimensionsService.findDimensions(dimensionsId);

        if (product.isPresent() && color.isPresent() && dimensions.isPresent()) {
            cart.removeProduct(product.get(), color.get(), dimensions.get());
        }

        session.setAttribute("cart", cart);
        return "redirect:/show-cart";
    }

    @GetMapping("/show-cart")
    public String showCart(HttpSession session, Model model) {
        Cart cart = getOrCreateCart(session);

        model.addAttribute("totalAmount", cart.getTotalAmount());
        model.addAttribute("cart", cart);
        return "cart";
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }



}