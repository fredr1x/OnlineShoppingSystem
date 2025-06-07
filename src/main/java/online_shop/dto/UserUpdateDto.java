package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import online_shop.validation.OnUpdate;

@Data
@NotNull(groups = OnUpdate.class)
public class UserUpdateDto {

    @NotNull(message = "id must be not null")
    private Long id;

    @NotNull(message = "first name must be not null")
    private String firstName;

    @NotNull(message = "last name must be not null")
    private String lastName;

    @Email(message = "email must be valid")
    @NotNull(message = "email must be not null")
    private String email;

    @NotNull(message = "password must be not null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


}
