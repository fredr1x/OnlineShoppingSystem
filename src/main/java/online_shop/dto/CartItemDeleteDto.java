package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class CartItemDeleteDto {

    private Long cartId;
    private Long cartItemId;

}
