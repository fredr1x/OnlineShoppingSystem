package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.OrderDto;
import online_shop.dto.OrderIdRequest;
import online_shop.dto.OrderRequestDto;
import online_shop.dto.UserIdRequest;
import online_shop.exception.*;
import online_shop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable OrderIdRequest orderId) throws OrderNotFoundException {
        return ResponseEntity.ok().body(orderService.getOrderById(orderId.getId()));
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestBody @Validated UserIdRequest userIdRequest) {
        return ResponseEntity.ok().body(orderService.getAllOrdersByUserId(userIdRequest.getId()));
    }

    @PostMapping("/make_order")
    public ResponseEntity<OrderDto> makeOrder(@RequestBody @Validated OrderRequestDto orderRequestDto) throws IllegalAccessException, CartIsEmptyException, IdMismatchException, UserNotFoundException {
        return ResponseEntity.ok().body(orderService.makeOrder(orderRequestDto));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderDto> payOrder(@RequestBody @Validated UserIdRequest userIdRequest, @PathVariable("orderId") Long orderId) throws OrderNotFoundException, UserNotFoundException, InsufficientFundsException, IdMismatchException {
        return ResponseEntity.ok().body(orderService.payOrder(userIdRequest.getId(), orderId));
    }

    @DeleteMapping("/{orderId}/cancel_order")
    public ResponseEntity<List<OrderDto>> cancelOrder(@PathVariable("orderId") Long orderId, @RequestBody UserIdRequest userIdRequest) throws OrderNotFoundException, UserNotFoundException, IdMismatchException, OrderAlreadyCancelledOrReturnedException {
        orderService.cancelOrder(orderId, userIdRequest.getId());
        return ResponseEntity.ok().body(orderService.getAllOrdersByUserId(userIdRequest.getId()));
    }


}
