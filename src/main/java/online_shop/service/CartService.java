package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.CartDto;
import online_shop.dto.CartItemDeleteDto;
import online_shop.dto.CartItemDto;
import online_shop.entity.Cart;
import online_shop.entity.CartItem;
import online_shop.exception.CartItemNotFound;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.CartItemMapper;
import online_shop.mapper.CartMapper;
import online_shop.repository.CartItemRepository;
import online_shop.repository.CartRepository;
import online_shop.repository.ProductRepository;
import online_shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartDto getCartByUserId(Long userId) {
        return cartMapper.toDto(cartRepository.findByUserId(userId));
    }

    public Integer getTotalItems(Long cartId) {
        return cartItemRepository.getTotalItems(cartId);
    }

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

        var totalPrice = cartItemRepository.getTotalPrice(cart.getId());
        var totalItems = cartItemRepository.getTotalItems(cart.getId());

        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
        cart.setModifiedAt(Instant.now());
        cartRepository.save(cart);

        return cartItemMapper.toDto(cartItemRepository.findAllByCartId(cart.getId()));
    }

    @Transactional
    public List<CartItemDto> deleteCartItem(CartItemDeleteDto cartItemDto) throws CartItemNotFound {

        var cartItem = cartItemRepository.findByCartId(cartItemDto.getCartItemId())
                .orElseThrow(() -> new CartItemNotFound("Cart item with id: " + cartItemDto.getCartItemId() + " not found"));

        cartItemRepository.delete(cartItem);

        return cartItemMapper.toDto(cartItemRepository.findAllByCartId(cartItem.getId()));
    }
}
