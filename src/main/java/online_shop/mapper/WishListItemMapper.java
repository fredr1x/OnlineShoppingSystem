package online_shop.mapper;

import online_shop.dto.WishListItemDto;
import online_shop.entity.WishListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishListItemMapper {

    @Mapping(source = "wishListId", target = "wishList.id")
    @Mapping(source = "productId", target = "product.id")
    WishListItem toEntity(WishListItemDto wishListItemDto);
    List<WishListItem> toEntity(List<WishListItemDto> wishListItemDtoList);

    @Mapping(source = "wishList.id", target = "wishListId")
    @Mapping(source = "product.id", target = "productId")
    WishListItemDto toDto(WishListItem wishListItem);
    List<WishListItemDto> toDto(List<WishListItem> list);
}
