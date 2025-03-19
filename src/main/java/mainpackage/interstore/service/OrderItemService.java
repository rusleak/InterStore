package mainpackage.interstore.service;

import lombok.RequiredArgsConstructor;
import mainpackage.interstore.model.OrderItem;
import mainpackage.interstore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

}
