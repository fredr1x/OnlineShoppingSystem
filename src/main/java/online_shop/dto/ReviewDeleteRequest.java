package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDeleteRequest {

    @NotNull
    @JsonProperty(value = "review_id")
    private Long reviewId;

}
