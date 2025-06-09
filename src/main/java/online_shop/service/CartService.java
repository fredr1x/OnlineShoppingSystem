package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.CartItemDto;
import online_shop.entity.Cart;
import online_shop.entity.CartItem;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.CartItemMapper;
import online_shop.repository.CartItemRepository;
import online_shop.repository.CartRepository;
import online_shop.repository.ProductRepository;
import online_shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public List<CartItemDto> addCartItem(CartItemDto cartItemDto) throws UserNotFoundException, ProductNotFoundException {
        var userId = cartItemDto.getUserId();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        var cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            var toSave = Cart.builder()
                    .user(user)
                    .createdAt(Instant.now())
                    .totalPrice(BigDecimal.ZERO)
                    .build();

            cart = cartRepository.save(toSave);
        }

        var product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + cartItemDto.getProductId() + " not found"));

        var toSave = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(cartItemDto.getQuantity())
                .build();

        cartItemRepository.save(toSave);
        return cartItemMapper.toDto(cartItemRepository.findByCartId(cart.getId()));
    }

}
