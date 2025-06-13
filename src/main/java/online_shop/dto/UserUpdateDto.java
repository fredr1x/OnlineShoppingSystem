package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {

    @NotNull(message = "id must be not null")
    private Long id;

    @JsonProperty(value = "first_name")
    @NotNull(message = "first name must be not null")
    private String firstName;

    @JsonProperty(value = "last_name")
    @NotNull(message = "last name must be not null")
    private String lastName;
}
