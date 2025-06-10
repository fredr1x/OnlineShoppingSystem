package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class WishListItemDeleteDto {

    private Long wishListId;

    private Long wishListItemId;

}
