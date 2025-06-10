package online_shop.mapper;

import online_shop.dto.WishListDto;
import online_shop.entity.WishList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishListMapper {

    @Mapping(source = "userId", target = "user.id")
    WishList toEntity(WishListDto wishListDto);

    @Mapping(source = "user.id", target = "userId")
    WishListDto toDto(WishList wishList);
}
