package online_shop.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import online_shop.security.JwtTokenFilter;
import online_shop.service.JwtTokenProvider;
import online_shop.service.OAuth2SuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ApplicationContext applicationContext;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            response.getWriter().write("Unauthorized.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(HttpStatus.FORBIDDEN.value());
                                            response.getWriter().write("Unauthorized.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers("/oauth2/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/login.html", "/register.html", "/static/**", "css/**", "js/**", "/styles.css", "/login.js", "/profile.html", "/profile.js", "/profile.css")
                                .permitAll()
                                .anyRequest().authenticated())
                .anonymous(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/profile.html", true)
                        .successHandler(oAuth2SuccessHandler))
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
