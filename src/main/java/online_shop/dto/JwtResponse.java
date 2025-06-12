package online_shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JwtResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
    private String accessToken;
    private String refreshToken;


}
