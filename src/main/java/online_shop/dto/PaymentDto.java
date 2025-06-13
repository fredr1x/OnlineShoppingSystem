package online_shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnUpdate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class PaymentDto {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private Instant paidAt;
}
