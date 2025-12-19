package online_shop.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import online_shop.service.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    ) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        System.out.println(path);
        if (path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/api/v1/auth/login")
            || path.startsWith("/api/v1/auth/register")
            || path.startsWith("/oauth2")) {

            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = httpRequest.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }

        if (bearerToken != null && jwtTokenProvider.isValid(bearerToken)) {
            Authentication authentication =
                    jwtTokenProvider.getAuthentication(bearerToken);

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
