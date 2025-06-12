package online_shop.controller;

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

    @PatchMapping("/admin/update/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable("id") Long id, @RequestBody @Validated UserUpdateRolesDto userDto) throws UserNotFoundException {
        if (!Objects.equals(id, userDto.getId())) throw new IllegalArgumentException("IDs must match");
        return ResponseEntity.ok().body(userService.updateRoles(id, userDto));
    }

    @PatchMapping("/change_password")
    public ResponseEntity<UserDto> changePassword(@RequestBody @Validated UserPasswordChangeDto userDto) throws UserNotFoundException, PasswordConfirmationException {
        return ResponseEntity.ok().body(userService.changePassword(userDto));
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Validated UserUpdateDto userDto) throws UserNotFoundException {
        return ResponseEntity.ok().body(userService.update(userDto));
    }

    // todo add method for balance
    @PatchMapping("/recharge")
    public ResponseEntity<UserDto> rechargeBalance(@RequestBody @Validated RechargeBalanceDto rechargeBalanceDto) throws UserNotFoundException, IllegalAmountOfRechargeException {
        return ResponseEntity.ok().body(userService.rechargeBalance(rechargeBalanceDto));
    }
}
