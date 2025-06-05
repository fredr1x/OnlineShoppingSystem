package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.JwtRequest;
import online_shop.dto.JwtResponse;
import online_shop.dto.UserDto;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.mapper.UserMapper;
import online_shop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    public JwtResponse refresh(String refreshToken) {
        return null;
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) throws EmailAlreadyUsedException {
        var user = userMapper.toEntity(userDto);

        if (userRepository.existsByEmail(userDto.getEmail())) throw new EmailAlreadyUsedException("Email: " + userDto.getEmail() + " is already in use");

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //user.setRole(Role.ROLE_USER);
        user.setRegisteredAt(Instant.now());

        var saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

}
