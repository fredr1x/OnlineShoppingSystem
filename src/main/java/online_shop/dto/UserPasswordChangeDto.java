package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordChangeDto {

    @NotNull
    private Long id;

    @JsonProperty(value = "old_password")
    @NotNull(message = "old password must be not null")
    private String oldPassword;

    @JsonProperty(value = "old_password_confirmation")
    @NotNull(message = "ols password confirmation must be not null")
    private String oldPasswordConfirmation;

    @JsonProperty(value = "new_password")
    @NotNull(message = "new password must be not null")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String newPassword;

}
