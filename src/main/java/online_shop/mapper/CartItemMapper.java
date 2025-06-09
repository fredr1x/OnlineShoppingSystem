package online_shop.mapper;

import online_shop.dto.CartItemDto;
import online_shop.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "cartId", target = "cart.id")
    @Mapping(source = "userId", target = "cart.user.id")
    @Mapping(source = "productId", target = "product.id")
    CartItem toEntity(CartItemDto cartItem);
    List<CartItem> toEntity(List<CartItemDto> cartItemDtos);

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "cart.user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    CartItemDto toDto(CartItem cartItem);
    List<CartItemDto> toDto(List<CartItem> cartItemDtos);

}
