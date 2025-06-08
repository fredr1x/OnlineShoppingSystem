package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordChangeDto {

    @NotNull
    private Long id;

    @NotNull(message = "old password must be not null")
    private String oldPassword;

    @NotNull(message = "ols password confirmation must be not null")
    private String oldPasswordConfirmation;

    @NotNull(message = "new password must be not null")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String newPassword;

}
