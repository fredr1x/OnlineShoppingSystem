package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.UserDto;
import online_shop.dto.UserUpdateDto;
import online_shop.exception.UserNotFoundException;
import online_shop.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.findUserById(id);
    }

    @GetMapping("/")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("/admin/update/{id}")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateDto userDto) throws UserNotFoundException {

        assert Objects.equals(id, userDto.getId());
        userService.update(id, userDto);

        return null;
    }
}
