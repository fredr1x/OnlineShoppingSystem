package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NotNull
public class CartTotalItemDto {
    private Long cartId;
}
