package online_shop.security;

import online_shop.entity.User;
import online_shop.entity.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JwtEntityFactory {

    public static JwtEntity createJwtEntity(User user) {
        return JwtEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(mapToGrantedAuthorities(user.getUserRoles()))
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<UserRoles> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRoles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getRoleValue().name()));
        }

        return authorities;
    }
}
