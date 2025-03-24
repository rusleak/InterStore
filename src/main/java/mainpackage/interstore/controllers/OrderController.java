package mainpackage.interstore.controllers;

import jakarta.servlet.http.HttpSession;
import mainpackage.interstore.model.Cart;
import mainpackage.interstore.model.CartItem;
import mainpackage.interstore.model.Order;
import mainpackage.interstore.model.OrderItem;
import mainpackage.interstore.model.DTOs.OrderRequestDTO;
import mainpackage.interstore.service.OrderItemService;
import mainpackage.interstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public String createOrder() {
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrder(HttpSession httpSession,
                              @ModelAttribute OrderRequestDTO orderRequestDTO,
                              HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        Order order = new Order();
        order.setFullName(orderRequestDTO.getFullName());
        order.setAddress(orderRequestDTO.getAddress());
        order.setEmail(orderRequestDTO.getEmail());
        order.setPhone(orderRequestDTO.getPhone());
        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        orderService.save(order);
        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartItem cartItem : cart.getItems().values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantityOrdered(cartItem.getQuantity());
            orderItem.setDimension(cartItem.getSelectedSize());
            orderItem.setColor(cartItem.getSelectedColor());
            orderItem.setOrder(order); // привязка к order
            orderItemList.add(orderItem);
        }

        order.getOrderItems().addAll(orderItemList);
        orderService.save(order);

        cart.clearCart();
        session.removeAttribute("cart");

        return "redirect:/order/success";
    }


}
