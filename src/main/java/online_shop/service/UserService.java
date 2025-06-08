package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.UserDto;
import online_shop.dto.UserPasswordChangeDto;
import online_shop.dto.UserUpdateDto;
import online_shop.dto.UserUpdateRolesDto;
import online_shop.entity.Role;
import online_shop.entity.User;
import online_shop.entity.UserRoles;
import online_shop.entity.enums.RoleValue;
import online_shop.exception.PasswordConfirmationException;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.UserMapper;
import online_shop.repository.RoleRepository;
import online_shop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto findUserById(Long id) throws UserNotFoundException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return userMapper.toDto(user);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public User getById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    public List<UserDto> findAllUsers() {
        return userMapper.toDto(userRepository.findAll());
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public UserDto updateRoles(Long id, UserUpdateRolesDto userDto) throws UserNotFoundException {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

        Set<UserRoles> roles = userDto.getRoles().stream()
                .map(role -> UserRoles.builder()
                        .user(user)
                        .role(roleRepository.findRole(role))
                        .build())
                .collect(Collectors.toSet());

        user.getUserRoles().clear();
        user.getUserRoles().addAll(roles);
        user.setModifiedAt(Instant.now());

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto changePassword(UserPasswordChangeDto userDto) throws UserNotFoundException, PasswordConfirmationException {

        if (!Objects.equals(userDto.getOldPassword(), userDto.getOldPasswordConfirmation())) throw new PasswordConfirmationException("Failed password confirmation");

        var user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userDto.getId() + " not found"));

        if (!passwordEncoder.matches(userDto.getOldPassword(), user.getPassword())) throw new PasswordConfirmationException("Failed password confirmation");

        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        user.setModifiedAt(Instant.now());
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto update(UserUpdateDto userDto) throws UserNotFoundException {

        var user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userDto.getId() + " not found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setModifiedAt(Instant.now());

        return userMapper.toDto(userRepository.save(user));
    }
}
