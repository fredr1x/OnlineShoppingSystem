package online_shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnUpdate;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentDto {

    @NotNull(message = "payment id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "order is must be not null")
    private Long orderId;

    @Min(value = 10, message = "min amount of payment is 10")
    @NotNull(message = "amount must be not null")
    private BigDecimal amount;
}
