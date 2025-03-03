package mainpackage.interstore.controllers;

import jakarta.servlet.http.HttpSession;
import mainpackage.interstore.model.Cart;
import mainpackage.interstore.model.Order;
import mainpackage.interstore.model.OrderItem;
import mainpackage.interstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Collectors;
@Controller
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("cart") Cart cart,
                              @RequestParam String fullName,
                              @RequestParam String phone,
                              @RequestParam String email,
                              @RequestParam String address,
                              HttpSession session) {
        Order order = new Order();
        order.setFullName(fullName);
        order.setPhone(phone);
        order.setEmail(email);
        order.setAddress(address);
        order.setOrderDate(Timestamp.from(Instant.now()));

        // Добавляем товары с выбранными цветами и размерами
        order.setOrderItems(cart.getItems().values().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantityOrdered(item.getQuantity());
            orderItem.setColor(item.getSelectedColor());
            orderItem.setDimension(item.getSelectedSize());
            return orderItem;
        }).collect(Collectors.toList()));

        orderService.save(order); // Сохраняем заказ в базе

        cart.clearCart(); // Очищаем корзину
        session.removeAttribute("cart"); // Удаляем корзину из сессии

        return "redirect:/order/success";
    }

}
