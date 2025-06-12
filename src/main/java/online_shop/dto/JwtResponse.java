package online_shop.dto;

import lombok.Data;

@Data
public class JwtResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String accessToken;
    private String refreshToken;


}
