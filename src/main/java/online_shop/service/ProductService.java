package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductDto;
import online_shop.entity.Product;
import online_shop.mapper.ProductMapper;
import online_shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
        entity.setImagePath("default");
        var saved = productRepository.save(entity);

        return productMapper.toDto(saved);
    }
}
