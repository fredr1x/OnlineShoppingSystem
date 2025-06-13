package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class UserIdRequest {

    @JsonProperty(value = "user_id")
    private Long id;

}
