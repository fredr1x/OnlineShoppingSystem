package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductIdRequest {

    @NotNull
    private final Long productId;

}
