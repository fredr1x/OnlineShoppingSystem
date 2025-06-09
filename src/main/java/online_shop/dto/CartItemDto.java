package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;

@Data
@Builder
public class CartItemDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @JsonProperty(value = "user_id")
    @NotNull(message = "user id must be not null", groups = {OnCreate.class, OnUpdate.class})
    private Long userId;

    @JsonProperty(value = "cart_id")
    private Long cartId;

    @JsonProperty(value = "product_id")
    @NotNull(message = "product id must be not null", groups = {OnCreate.class, OnUpdate.class})
    private Long productId;

    @NotNull(message = "quantity must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;

}
