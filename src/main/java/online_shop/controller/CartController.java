package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.CartDto;
import online_shop.dto.CartItemDeleteDto;
import online_shop.dto.CartItemDto;
import online_shop.exception.CartItemNotFound;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.service.CartService;
import online_shop.validation.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Cart",
        description = "Операции с корзиной пользователя"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Получить корзину пользователя",
            description = "Возвращает корзину пользователя по его userId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Корзина успешно получена",
            content = @Content(schema = @Schema(implementation = CartDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь или корзина не найдены"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable("id") Long userId) {
        return ResponseEntity.ok().body(cartService.getCartByUserId(userId));
    }

    @Operation(
            summary = "Получить список товаров в корзине пользователя",
            description = "Возвращает список товаров в корзине пользователя по его userId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Товары в корзине успешно получены",
            content = @Content(schema = @Schema(implementation = CartDto.class))
    )
    @GetMapping("/{id}/cart-items")
    public ResponseEntity<List<CartItemDto>> getAllCartItems(@PathVariable("id") Long userId) {
        return ResponseEntity.ok().body(cartService.getCartItems(userId));
    }

    @Operation(
            summary = "Получить общее количество товаров в корзине",
            description = "Возвращает суммарное количество товаров в корзине по cartId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Количество товаров успешно получено",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Integer.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Корзина не найдена"
    )
    @GetMapping("/{id}/total_cart_items")
    public ResponseEntity<Integer> totalCartItems(@PathVariable("id") Long cartId) {
        return ResponseEntity.ok().body(cartService.getTotalItems(cartId));
    }

    @Operation(
            summary = "Добавить товар в корзину",
            description = "Добавляет товар в корзину пользователя и возвращает обновлённый список позиций"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Товар успешно добавлен",
            content = @Content(schema = @Schema(implementation = CartItemDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь или продукт не найдены"
    )
    @PostMapping("/add_item")
    public ResponseEntity<List<CartItemDto>> addCartItem(@RequestBody @Validated(OnCreate.class) CartItemDto cartItemDto) throws UserNotFoundException, ProductNotFoundException {
        return ResponseEntity.ok().body(cartService.addCartItem(cartItemDto));
    }

    @Operation(
            summary = "Удалить товар из корзины",
            description = "Удаляет товар из корзины и возвращает обновлённый список позиций"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Товар успешно удалён",
            content = @Content(schema = @Schema(implementation = CartItemDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Товар в корзине не найден"
    )
    @DeleteMapping("/delete_item")
    public ResponseEntity<List<CartItemDto>> deleteCartItem(@RequestBody @Validated CartItemDeleteDto cartItemDto) throws CartItemNotFound {
        return ResponseEntity.ok().body(cartService.deleteCartItem(cartItemDto));
    }
}
