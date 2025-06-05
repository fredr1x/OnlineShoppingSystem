package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.JwtRequest;
import online_shop.dto.JwtResponse;
import online_shop.dto.UserDto;
import online_shop.entity.Role;
import online_shop.entity.UserRoles;
import online_shop.entity.enums.RoleValue;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    public JwtResponse login(JwtRequest loginRequest) throws UserNotFoundException {
        JwtResponse jwtResponse = new JwtResponse();
        var authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        authenticationManager.authenticate(authentication);

        var user = userService.getUserByEmail(loginRequest.getEmail());

        var userId = user.getId();
        var email = user.getEmail();
        var roles = user.getUserRoles();

        jwtResponse.setId(userId);
        jwtResponse.setEmail(email);
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(userId, email, roles));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(userId, email));

        return jwtResponse;
    }

    public JwtResponse refresh(String refreshToken) throws UserNotFoundException {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) throws EmailAlreadyUsedException {
        var user = userMapper.toEntity(userDto);

        if (userService.existsByEmail(userDto.getEmail())) throw new EmailAlreadyUsedException("Email: " + userDto.getEmail() + " is already in use");

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        var userRoles = UserRoles.builder()
                .user(user)
                .role(Role.builder().roleValue(RoleValue.ROLE_USER).build())
                .build();

        user.setRole(userRoles);
        user.setRegisteredAt(Instant.now());

        var saved = userService.save(user);
        return userMapper.toDto(saved);
    }
}
