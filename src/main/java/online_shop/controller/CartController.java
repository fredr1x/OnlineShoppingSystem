package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.CartDto;
import online_shop.dto.CartItemDeleteDto;
import online_shop.dto.CartItemDto;
import online_shop.dto.CartTotalItemDto;
import online_shop.exception.CartItemNotFound;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.service.CartService;
import online_shop.validation.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable("id") Long userId) {
        return ResponseEntity.ok().body(cartService.getCartByUserId(userId));
    }

    @GetMapping("/total_cart_items")
    public ResponseEntity<Integer> totalCartItems(@RequestBody @Validated CartTotalItemDto cartItemDto) {
        return ResponseEntity.ok().body(cartService.getTotalItems(cartItemDto.getCartId()));
    }

    @PostMapping("/add_item")
    public ResponseEntity<List<CartItemDto>> addCartItem(@RequestBody @Validated(OnCreate.class) CartItemDto cartItemDto) throws UserNotFoundException, ProductNotFoundException {
        return ResponseEntity.ok().body(cartService.addCartItem(cartItemDto));
    }

    @DeleteMapping("/delete_item")
    public ResponseEntity<List<CartItemDto>> deleteCartItem(@RequestBody @Validated CartItemDeleteDto cartItemDto) throws CartItemNotFound {
        return ResponseEntity.ok().body(cartService.deleteCartItem(cartItemDto));
    }
}
