package mainpackage.interstore.repository;

import mainpackage.interstore.model.OrderItem;
import mainpackage.interstore.model.util.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
