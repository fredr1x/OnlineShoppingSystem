package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnUpdate;

import java.math.BigDecimal;

@Data
@Builder
public class ProductPriceDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "price must be not null", groups = OnUpdate.class)
    private BigDecimal price;

}
