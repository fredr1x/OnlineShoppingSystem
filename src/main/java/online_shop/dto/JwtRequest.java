package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import online_shop.validation.OnCreate;

@Data
public class JwtRequest {

    @Email(message = "email must be valid")
    @NotNull(message = "email must be not null", groups = OnCreate.class)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "password must be not null", groups = OnCreate.class)
    private String password;

}
