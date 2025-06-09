package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartDto {

    @NotNull
    private Long id;

    @NotNull
    private BigDecimal totalPrice;

}
