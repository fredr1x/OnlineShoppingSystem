package online_shop.mapper;

import online_shop.dto.CartDto;
import online_shop.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    Cart toEntity(CartDto cartDto);

    CartDto toDto(Cart cart);
}
