package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishListDto {

    @NotNull
    private Long id;

    @NotNull
    @JsonProperty(value = "user_id")
    private Long userId;
}
