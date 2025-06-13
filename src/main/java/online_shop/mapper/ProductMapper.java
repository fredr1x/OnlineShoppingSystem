package online_shop.mapper;

import online_shop.dto.ProductDto;
import online_shop.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto dto);

    List<Product> toEntity(List<ProductDto> dtos);

    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> products);
}
