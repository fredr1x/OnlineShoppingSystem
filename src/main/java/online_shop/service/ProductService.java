package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductCategoryDto;
import online_shop.dto.ProductDto;
import online_shop.dto.ProductStockDto;
import online_shop.entity.enums.Category;
import online_shop.mapper.ProductMapper;
import online_shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Transactional
    public ProductDto addProduct(ProductDto product) {

        var entity = productMapper.toEntity(product);
        entity.setCreatedAt(Instant.now());
        entity.setRating(0.0F);
        var saved = productRepository.save(entity);

        return productMapper.toDto(saved);
    }

    @Transactional
    public ProductStockDto changeStock(ProductStockDto productStock) {

        var entity = productRepository.findById(productStock.getId())
                .orElseThrow();

        entity.setStock(productStock.getStock());
        entity.setUpdatedAt(Instant.now());
        var saved = productRepository.save(entity);
        return ProductStockDto.builder()
                .id(saved.getId())
                .stock(saved.getStock())
                .build();
    }

    public List<ProductDto> getAllProductsByCategory(ProductCategoryDto category) {
        return productMapper
                .toDto(productRepository
                .findByCategory(Category.valueOf(category.getCategory().toUpperCase())));
    }

    public List<ProductDto> getTop10Products() {
        return productMapper.toDto(productRepository.getTop10Products());
    }
}
