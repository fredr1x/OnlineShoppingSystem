package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long cartId;

}
