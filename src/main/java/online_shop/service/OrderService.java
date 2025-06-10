package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.OrderDto;
import online_shop.dto.OrderProductDto;
import online_shop.dto.OrderRequestDto;
import online_shop.entity.Order;
import online_shop.entity.OrderItem;
import online_shop.entity.Payment;
import online_shop.entity.enums.OrderStatus;
import online_shop.entity.enums.PaymentMethod;
import online_shop.entity.enums.PaymentStatus;
import online_shop.exception.InsufficientFundsException;
import online_shop.exception.OrderNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Transactional
    public OrderDto makeOrder(OrderRequestDto orderRequestDto) {

        var cart = cartRepository.findByUserId(orderRequestDto.getCartId());
        var cartItems = cartItemRepository.findAllByCartId(cart.getId());
        var totalPrice = cartItemRepository.getTotalPrice(cart.getId());

        var order = Order.builder()
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        List<OrderProductDto> orderItems = new ArrayList<>();

        for (var items: cartItems) {

            var orderItem = OrderItem.builder()
                    .order(order)
                    .product(items.getProduct())
                    .quantity(items.getQuantity())
                    .priceAtPurchase(items.getProduct().getPrice())
                    .build();

            orderItemsRepository.save(orderItem);

            var orderItemToReturn = OrderProductDto.builder()
                    .productId(items.getId())
                    .productPrice(items.getProduct().getPrice())
                    .quantity(items.getQuantity())
                    .build();

            orderItems.add(orderItemToReturn);
        }

        orderRepository.save(order);

        return OrderDto.builder()
                .id(order.getId())
                .orderStatus(order.getStatus().name())
                .products(orderItems)
                .build();
    }

    @Transactional
    public OrderDto payOrder(Long userId, Long orderId) throws OrderNotFoundException, UserNotFoundException, InsufficientFundsException {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not found"));

        if (!Objects.equals(order.getUser().getId(), userId)
            && order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("User id mismatch");
        }

        if (user.getBalance().compareTo(order.getTotalPrice()) < 0) {
            throw new InsufficientFundsException("Not enough balance to pay order with id: " + order.getId());
        }

        user.setBalance(user.getBalance().subtract(order.getTotalPrice()));
        userRepository.save(user);

        order.setStatus(OrderStatus.PAID);
        var saved = orderRepository.save(order);

        var payment = Payment.builder()
                .order(saved)
                .paymentMethod(PaymentMethod.SYSTEM)
                .amount(saved.getTotalPrice())
                .createdAt(Instant.now())
                .paidAt(Instant.now())
                .status(PaymentStatus.PAID)
                .build();

        paymentRepository.save(payment);

        getOrderItems(order);

        cartItemRepository.deleteAllByCartId(user.getCart().getId());

        return OrderDto.builder()
                .id(saved.getId())
                .orderStatus(order.getStatus().name())
                .products(getOrderItems(order))
                .build();
    }

    public List<OrderDto> getAllOrdersByUserId(Long userId) {

        var orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderDto> orderDtos = new ArrayList<>();

        for (var order: orders) {

            getOrderItems(order);
            List<OrderProductDto> products = getOrderItems(order);
            var orderDto = OrderDto.builder()
                    .id(order.getId())
                    .orderStatus(order.getStatus().name())
                    .products(products)
                    .build();

            orderDtos.add(orderDto);
        }

        return orderDtos;
    }

    public OrderDto getOrderById(Long orderId) throws OrderNotFoundException {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not found"));

        return OrderDto.builder()
                .id(order.getId())
                .orderStatus(order.getStatus().name())
                .products(getOrderItems(order))
                .build();
    }

    private List<OrderProductDto> getOrderItems(Order order) {

        var orderItems = orderItemsRepository.findByOrderId(order.getId());

        List<OrderProductDto> products = new ArrayList<>();
        for (var item: orderItems) {
            var orderItemsToReturn = OrderProductDto.builder()
                    .productId(item.getProduct().getId())
                    .productPrice(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .build();
            products.add(orderItemsToReturn);
        }

        return products;
    }
}
