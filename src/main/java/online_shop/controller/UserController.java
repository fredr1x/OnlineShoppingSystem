package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.*;
import online_shop.exception.IllegalAmountOfRechargeException;
import online_shop.exception.PasswordConfirmationException;
import online_shop.exception.UserNotFoundException;
import online_shop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(
        name = "Users",
        description = "Управление пользователями, профилем и балансом"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @GetMapping("/{id}")
    public UserDto findUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("id") Long id
    ) throws UserNotFoundException {
        return userService.findUserById(id);
    }

    @Operation(
            summary = "Получить список всех пользователей",
            description = "Возвращает список всех пользователей (доступно только администратору)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей получен",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав доступа"
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @Operation(
            summary = "Обновить роли пользователя",
            description = "Изменяет роли пользователя (только для администратора)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Роли пользователя обновлены",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID в пути и теле запроса не совпадают"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @PatchMapping("/admin/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUserRole(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления ролей пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateRolesDto.class))
            )
            @RequestBody @Validated UserUpdateRolesDto userDto
    ) throws UserNotFoundException {

        if (!Objects.equals(id, userDto.getId())) {
            throw new IllegalArgumentException("IDs must match");
        }
        return ResponseEntity.ok(userService.updateRoles(id, userDto));
    }

    @Operation(
            summary = "Сменить пароль",
            description = "Изменяет пароль пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пароль успешно изменён",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка подтверждения пароля"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @PatchMapping("/change_password")
    public ResponseEntity<UserDto> changePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для смены пароля",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserPasswordChangeDto.class))
            )
            @RequestBody @Validated UserPasswordChangeDto userDto
    ) throws UserNotFoundException, PasswordConfirmationException {

        return ResponseEntity.ok(userService.changePassword(userDto));
    }

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновляет профиль пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные пользователя обновлены",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновлённые данные пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateDto.class))
            )
            @RequestBody @Validated UserUpdateDto userDto
    ) throws UserNotFoundException {

        return ResponseEntity.ok(userService.update(userDto));
    }

    @Operation(
            summary = "Пополнить баланс пользователя",
            description = "Пополняет баланс пользователя на указанную сумму"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Баланс успешно пополнен",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректная сумма пополнения"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @PatchMapping("/recharge")
    public ResponseEntity<UserDto> rechargeBalance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для пополнения баланса",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RechargeBalanceDto.class))
            )
            @RequestBody @Validated RechargeBalanceDto rechargeBalanceDto
    ) throws UserNotFoundException, IllegalAmountOfRechargeException {

        return ResponseEntity.ok(userService.rechargeBalance(rechargeBalanceDto));
    }
}
