package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.UserDto;
import online_shop.entity.enums.Role;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.UserMapper;
import online_shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto findUserById(Long id) throws UserNotFoundException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) throws EmailAlreadyUsedException {
        var user = userMapper.toEntity(userDto);

        if (userRepository.existsByEmail(userDto.getEmail())) throw new EmailAlreadyUsedException("Email: " + userDto.getEmail() + " is already in use");

        user.setRole(Role.ROLE_USER);
        user.setRegisteredAt(Instant.now());

        var saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

}
