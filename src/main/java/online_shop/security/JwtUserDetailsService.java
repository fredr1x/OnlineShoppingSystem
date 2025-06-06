package online_shop.security;

import lombok.RequiredArgsConstructor;
import online_shop.exception.UserNotFoundException;
import online_shop.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            var user = userService.getUserByEmail(email);
            return JwtEntityFactory.createJwtEntity(user);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
