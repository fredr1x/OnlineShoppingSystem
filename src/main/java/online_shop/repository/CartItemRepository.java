package online_shop.repository;

import online_shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("select ci from CartItem ci where ci.cart.id=:cartId")
    Optional<CartItem> findByCartId(Long cartId);

    @Query("select ci from CartItem ci where ci.cart.id=:cartId")
    List<CartItem> findAllByCartId(Long cartId);

    @Query("select sum(ci.quantity * ci.product.price) from CartItem ci where ci.cart.id=:cartId")
    BigDecimal getTotalPrice(Long cartId);

    @Query("select sum(ci.id * ci.quantity) from CartItem ci where ci.cart.id=:cartId")
    Integer getTotalItems(Long cartId);
}
