package online_shop.service;

import online_shop.dto.JwtRequest;
import online_shop.dto.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    public JwtResponse refresh(String refreshToken) {
        return null;
    }

}
