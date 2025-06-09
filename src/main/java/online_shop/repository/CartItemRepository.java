package online_shop.repository;

import online_shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("select ci from CartItem ci where ci.cart.id=:cartId")
    List<CartItem> findByCartId(Long cartId);
}
