package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {

    @NotNull(message = "id must be not null")
    private Long id;

    @NotNull(message = "first name must be not null")
    private String firstName;

    @NotNull(message = "last name must be not null")
    private String lastName;
}
