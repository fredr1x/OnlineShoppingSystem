package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.OrderDto;
import online_shop.dto.OrderIdRequest;
import online_shop.dto.OrderRequestDto;
import online_shop.dto.UserIdRequest;
import online_shop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Orders",
        description = "Управление заказами: получение, создание, оплата и отмена"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Получить заказ по ID",
            description = "Возвращает информацию о заказе по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заказ успешно найден",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Заказ не найден"
            )
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @Parameter(description = "ID заказа", required = true)
            @PathVariable OrderIdRequest orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId.getId()));
    }

    @Operation(
            summary = "Получить все заказы пользователя",
            description = "Возвращает список всех заказов конкретного пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список заказов успешно получен",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrders(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userId));
    }

    @Operation(
            summary = "Создать заказ",
            description = "Создаёт новый заказ на основе переданных данных"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заказ успешно создан",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные заказа"
            )
    })
    @PostMapping("/make_order")
    public ResponseEntity<OrderDto> makeOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания заказа",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderRequestDto.class))
            )
            @RequestBody @Validated OrderRequestDto orderRequestDto
    ) {
        return ResponseEntity.ok(orderService.makeOrder(orderRequestDto));
    }

    @Operation(
            summary = "Оплатить заказ",
            description = "Оплачивает заказ от имени пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заказ успешно оплачен",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь не имеет прав на оплату заказа"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Заказ не найден"
            )
    })
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderDto> payOrder(
            @Parameter(description = "ID заказа", required = true)
            @PathVariable("orderId") Long orderId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID пользователя, который оплачивает заказ",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserIdRequest.class))
            )
            @RequestBody @Validated UserIdRequest userIdRequest
    ) {
        return ResponseEntity.ok(orderService.payOrder(userIdRequest.getId(), orderId));
    }

    @Operation(
            summary = "Отменить заказ",
            description = "Отменяет заказ и возвращает обновлённый список заказов пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заказ успешно отменён",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь не имеет прав на отмену заказа"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Заказ не найден"
            )
    })
    @DeleteMapping("/{orderId}/cancel_order")
    public ResponseEntity<List<OrderDto>> cancelOrder(
            @Parameter(description = "ID заказа", required = true)
            @PathVariable("orderId") Long orderId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID пользователя, который отменяет заказ",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserIdRequest.class))
            )
            @RequestBody UserIdRequest userIdRequest
    ) {
        orderService.cancelOrder(orderId, userIdRequest.getId());
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userIdRequest.getId()));
    }
}
