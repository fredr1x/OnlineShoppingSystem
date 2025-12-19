package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.JwtRequest;
import online_shop.dto.JwtResponse;
import online_shop.dto.UserDto;
import online_shop.entity.Cart;
import online_shop.entity.UserRoles;
import online_shop.entity.WishList;
import online_shop.exception.EmailAlreadyUsedException;
import online_shop.mapper.UserMapper;
import online_shop.repository.CartRepository;
import online_shop.repository.RoleRepository;
import online_shop.repository.UserRolesRepository;
import online_shop.repository.WishListRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserMapper userMapper;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final WishListRepository wishListRepository;
    private final UserRolesRepository userRolesRepository;

    public JwtResponse login(JwtRequest loginRequest) {
        var user = userService.getUserByEmail(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password mismatch!");
        }

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(user.getId());
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setFirstName(user.getFirstName());
        jwtResponse.setLastName(user.getLastName());
        jwtResponse.setBalance(user.getBalance());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getUserRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail()));

        System.out.println(jwtResponse);
        return jwtResponse;
    }

    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) {
        var user = userMapper.toEntity(userDto);
        if (userService.existsByEmail(userDto.getEmail()))
            throw new EmailAlreadyUsedException("Email: " + userDto.getEmail() + " is already in use");

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRegisteredAt(Instant.now());

        var savedUser = userService.save(user);

        var role = roleRepository.findUserRole();
        var userRoles = UserRoles.builder()
                .user(savedUser)
                .role(role)
                .build();
        userRolesRepository.save(userRoles);

        savedUser.setRole(userRoles);

        var cart = Cart.builder()
                .user(savedUser)
                .totalPrice(BigDecimal.ZERO)
                .totalItems(0)
                .createdAt(Instant.now())
                .build();
        cartRepository.save(cart);

        var wishList = WishList.builder()
                .user(savedUser)
                .build();
        wishListRepository.save(wishList);

        return userMapper.toDto(savedUser);
    }
}
