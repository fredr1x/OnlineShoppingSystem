package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.WishListItemDeleteDto;
import online_shop.dto.WishListItemDto;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.exception.WishListItemNotFound;
import online_shop.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Wish Lists",
        description = "Список желаемых товаров пользователя"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wish_lists")
public class WishListController {

    private final WishListService wishListService;

    @Operation(
            summary = "Получить список желаемых товаров пользователя",
            description = "Возвращает wishlist пользователя по его ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Wishlist успешно получен",
                    content = @Content(schema = @Schema(implementation = WishListItemDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<WishListItemDto>> getWishListByUserId(
            @Parameter(description = "ID пользователя", required = true, example = "3")
            @PathVariable("id") Long userId
    ) throws UserNotFoundException {

        return ResponseEntity.ok(wishListService.getWishListByUserId(userId));
    }

    @Operation(
            summary = "Добавить товар в wishlist",
            description = "Добавляет товар в список желаемых пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Товар добавлен в wishlist",
                    content = @Content(schema = @Schema(implementation = WishListItemDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар не найден"
            )
    })
    @PostMapping("/add_item")
    public ResponseEntity<List<WishListItemDto>> addWishListItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные товара для добавления в wishlist",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WishListItemDto.class))
            )
            @RequestBody @Validated WishListItemDto wishListItemDto
    ) throws ProductNotFoundException {

        return ResponseEntity.ok(wishListService.addWishListItem(wishListItemDto));
    }

    @Operation(
            summary = "Удалить товар из wishlist",
            description = "Удаляет товар из списка желаемых пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Товар удалён из wishlist",
                    content = @Content(schema = @Schema(implementation = WishListItemDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар в wishlist не найден"
            )
    })
    @DeleteMapping("/delete_item")
    public ResponseEntity<List<WishListItemDto>> deleteWishListItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для удаления товара из wishlist",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WishListItemDeleteDto.class))
            )
            @RequestBody WishListItemDeleteDto wishListItemDto
    ) throws WishListItemNotFound {

        return ResponseEntity.ok(wishListService.deleteWishListItem(wishListItemDto));
    }
}
