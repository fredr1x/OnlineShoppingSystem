package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.PaymentDto;
import online_shop.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Payments",
        description = "Операции с платежами пользователей"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Получить все платежи пользователя",
            description = "Возвращает список всех платежей, совершённых пользователем"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Платежи успешно получены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Нет прав доступа к платежам пользователя"
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByUserId(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(paymentService.getAllPaymentsByUserId(userId));
    }
}
