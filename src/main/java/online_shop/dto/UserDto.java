package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;

import java.math.BigDecimal;

@Data
@Builder
public class UserDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "first name must be not null", groups = OnCreate.class)
    private String firstName;

    @NotNull(message = "last name must be not null", groups = OnCreate.class)
    private String lastName;

    @Email(message = "email must be valid")
    @NotNull(message = "email must be not null", groups = OnCreate.class)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "password must be not null", groups = OnCreate.class)
    private String password;

    @Min(value = 0)
    @NotNull(message = "balance must be not null")
    private BigDecimal balance;
}
