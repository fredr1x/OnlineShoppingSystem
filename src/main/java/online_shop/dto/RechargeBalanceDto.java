package online_shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeBalanceDto {


    @NotNull
    private Long userId;

    @Min(value = 10)
    @NotNull
    private BigDecimal amount;
}
