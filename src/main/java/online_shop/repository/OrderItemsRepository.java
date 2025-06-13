package online_shop.repository;

import online_shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi where oi.order.id=:orderId")
    List<OrderItem> findByOrderId(Long orderId);
}
