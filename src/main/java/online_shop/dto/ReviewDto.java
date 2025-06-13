package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;

@Data
@Builder
public class ReviewDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @JsonProperty(value = "user_id")
    @NotNull(message = "user id must be not null", groups = {OnCreate.class, OnUpdate.class})
    private Long userId;

    @JsonProperty(value = "product_id")
    @NotNull(message = "product id must be not null", groups = {OnCreate.class, OnUpdate.class})
    private Long productId;

    @Min(value = 1, message = "min rating is 1")
    @Max(value = 5, message = "max rating is 5")
    @NotNull(message = "rating must be not null", groups = {OnCreate.class, OnUpdate.class})
    private Integer rating;

    private String comment;
}
