package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.PaymentDto;
import online_shop.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(paymentService.getAllPaymentsByUserId(userId));
    }

}
