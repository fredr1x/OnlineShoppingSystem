package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishListItemDto {

    @NotNull(message = "id must be not null")
    private Long id;

    @NotNull(message = "wish list id must be not null")
    private Long wishListId;

    @NotNull(message = "product id must be not null")
    private Long productId;
}
