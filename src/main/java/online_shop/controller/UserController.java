package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.UserDto;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.exception.UserNotFoundException;
import online_shop.service.UserService;
import online_shop.validation.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.findUserById(id);
    }
}
