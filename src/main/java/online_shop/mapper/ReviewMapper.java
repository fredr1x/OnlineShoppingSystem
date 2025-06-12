package online_shop.mapper;

import online_shop.dto.ReviewDto;
import online_shop.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "productId", target = "product.id")
    Review toEntity(ReviewDto reviewDto);
    List<Review> toEntity(List<ReviewDto> reviewDtoList);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    ReviewDto toDto(Review review);
    List<ReviewDto> toDto(List<Review> reviewList);

}
