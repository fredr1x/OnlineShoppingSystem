package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class OrderIdRequest {

    private Long id;

}
