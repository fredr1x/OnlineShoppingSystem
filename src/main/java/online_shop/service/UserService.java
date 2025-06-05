package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.UserDto;
import online_shop.exception.UserNotFoundException;
import online_shop.mapper.UserMapper;
import online_shop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto findUserById(Long id) throws UserNotFoundException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return userMapper.toDto(user);
    }
}
