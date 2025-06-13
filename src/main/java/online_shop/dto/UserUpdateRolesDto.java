package online_shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import online_shop.entity.enums.RoleValue;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserUpdateRolesDto {

    @NotNull(message = "id must be not null")
    private Long id;

    @Builder.Default
    @NotNull(message = "roles must be not null")
    @Size(min = 1, message = "at least one role must be provided")
    Set<RoleValue> roles = new HashSet<>();
}
