package online_shop.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online_shop.entity.User;
import online_shop.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken auth2Authentication = (OAuth2AuthenticationToken) authentication;

        String email = auth2Authentication.getPrincipal().getAttribute("email");

        User user = null;
        try {
            user = userService.getUserByEmail(email);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (user == null) {
            response.sendRedirect("/login.html");
            return;
        }

        var userId = user.getId();
        var userEmail = user.getEmail();
        var userRoles = user.getUserRoles();

        String token = jwtTokenProvider.createAccessToken(userId, userEmail, userRoles);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofHours(1))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect("/home");
    }
}
