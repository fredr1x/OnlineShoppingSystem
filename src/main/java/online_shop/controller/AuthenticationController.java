package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.JwtRequest;
import online_shop.dto.JwtResponse;
import online_shop.dto.UserDto;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.exception.UserNotFoundException;
import online_shop.service.AuthenticationService;
import online_shop.validation.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Validated JwtRequest loginRequest) throws UserNotFoundException {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated(OnCreate.class) UserDto user) throws EmailAlreadyUsedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registerUser(user));
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) throws UserNotFoundException {
        return authenticationService.refresh(refreshToken);
    }

}

