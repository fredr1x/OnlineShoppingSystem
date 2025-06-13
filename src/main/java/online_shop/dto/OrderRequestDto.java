package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDto {

    @NotNull
    @JsonProperty(value = "user_id")
    private Long userId;

    @NotNull
    @JsonProperty(value = "cart_id")
    private Long cartId;

}
