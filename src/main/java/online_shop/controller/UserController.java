package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.UserDto;
import online_shop.entity.enums.Role;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.UserMapper;
import online_shop.repository.UserRepository;
import online_shop.validation.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return userMapper.toDto(user);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Validated(OnCreate.class) UserDto userDto) throws EmailAlreadyUsedException {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyUsedException("Email already in use");
        }

        var user = userMapper.toEntity(userDto);
        user.setRole(Role.ROLE_USER);

        var saved = userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toDto(saved));
    }


}
