package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.JwtRequest;
import online_shop.dto.JwtResponse;
import online_shop.dto.UserDto;
import online_shop.service.AuthenticationService;
import online_shop.validation.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Authentication",
        description = "Аутентификация, регистрация и обновление JWT-токенов"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Вход пользователя",
            description = "Аутентификация пользователя по email и паролю. Возвращает access и refresh JWT токены."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешная аутентификация",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден"
    )
    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Validated JwtRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создание нового пользователя. Email должен быть уникальным."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно зарегистрирован",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "Email уже используется"
    )
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated(OnCreate.class) UserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registerUser(user));
    }

    @Operation(
            summary = "Обновление access токена",
            description = "Обновляет access JWT токен по refresh токену. Refresh токен должен быть валидным."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Access токен успешно обновлён",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден или refresh токен невалиден"
    )
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authenticationService.refresh(refreshToken);
    }
}

