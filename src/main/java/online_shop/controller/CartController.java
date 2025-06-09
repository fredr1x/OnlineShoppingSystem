package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.CartItemDto;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.service.CartService;
import online_shop.validation.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add_item")
    public ResponseEntity<List<CartItemDto>> addCartItem(@RequestBody @Validated(OnCreate.class) CartItemDto cartItemDto) throws UserNotFoundException, ProductNotFoundException {
        return ResponseEntity.ok().body(cartService.addCartItem(cartItemDto));
    }



}
