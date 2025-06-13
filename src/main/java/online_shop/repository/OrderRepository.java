package online_shop.repository;

import online_shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @Query("select count(*) > 0 " +
           "from OrderItem oi " +
           "join Order o " +
           "on o.id = oi.order.id " +
           "where o.user.id=:userId " +
           "and oi.product.id=:productId " +
           "and o.status='COMPLETED'")
    boolean hasUserPurchasedProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
