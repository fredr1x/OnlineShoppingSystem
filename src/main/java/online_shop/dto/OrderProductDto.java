package online_shop.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderProductDto {

    private Long productId;

    private BigDecimal productPrice;

    private Integer quantity;

}
