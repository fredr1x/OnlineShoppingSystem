package online_shop.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductCategoryDto;
import online_shop.dto.ProductDto;
import online_shop.dto.ProductPriceDto;
import online_shop.dto.ProductStockDto;
import online_shop.entity.Product;
import online_shop.entity.enums.Category;
import online_shop.exception.ProductNotFoundException;
import online_shop.mapper.ProductMapper;
import online_shop.repository.ProductRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final MinioService minioService;

    public ProductDto getProductById(Long id) throws ProductNotFoundException {
        return productMapper.toDto(productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found")));
    }

    public List<ProductDto> getAllProductsByCategory(ProductCategoryDto category) {
        return productMapper
                .toDto(productRepository
                        .findByCategory(Category.valueOf(category.getCategory().toUpperCase())));
    }

    public List<ProductDto> getTop10Products() {
        return productMapper.toDto(productRepository.getTop10Products());
    }

    public List<ProductDto> searchByKeyword(String keyword) {
        return productMapper.toDto(productRepository.searchByKeyword(keyword));
    }

    public List<ProductDto> filter(Float ratingAbove, BigDecimal minPrice, BigDecimal maxPrice, ProductCategoryDto category) {
        List<Product> products = productRepository.findAll((root, query, cb) -> {
           List<Predicate> predicates = new ArrayList<>();

           if (ratingAbove != null) {
               predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), ratingAbove));
           }

           if (minPrice != null) {
               predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
           }

           if (maxPrice != null) {
               predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
           }

           if (category != null) {
               predicates.add(cb.equal(root.get("category"), Category.valueOf(category.getCategory())));
           }

            return cb.and(predicates.toArray(new Predicate[0]));
        });

        return productMapper.toDto(products);
    }

    @Transactional
    public ProductDto addProduct(ProductDto product, MultipartFile image) {

        var entity = productMapper.toEntity(product);
        entity.setCreatedAt(Instant.now());
        entity.setRating(0.0F);

        var saved = productRepository.save(entity);

        String imagePath = "products/%d/%s".formatted(
                saved.getId(),
                image.getOriginalFilename()
        );

        try {
            minioService.uploadFile(imagePath, image);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка загрузки изображения", e);
        }

        saved.setImagePath(imagePath);

        return productMapper.toDto(saved);
    }

    @Transactional
    public ProductStockDto changeStock(ProductStockDto productStock) throws ProductNotFoundException {

        var entity = productRepository.findById(productStock.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + productStock.getId() + "not found"));

        entity.setStock(productStock.getStock());
        entity.setUpdatedAt(Instant.now());
        var saved = productRepository.save(entity);
        return ProductStockDto.builder()
                .id(saved.getId())
                .stock(saved.getStock())
                .build();
    }

    @Transactional
    public ProductPriceDto changePrice(ProductPriceDto productPrice) throws ProductNotFoundException {

        var entity = productRepository.findById(productPrice.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + productPrice.getId() + "not found"));

        entity.setPrice(productPrice.getPrice());
        entity.setUpdatedAt(Instant.now());
        var saved = productRepository.save(entity);
        return ProductPriceDto.builder()
                .id(saved.getId())
                .price(saved.getPrice())
                .build();
    }

    @Transactional
    public void deleteById(Long id) throws ProductNotFoundException {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));

        productRepository.delete(product);
    }

    @Transactional
    public ProductDto updateProduct(ProductDto dto) throws ProductNotFoundException {

        var existing = productRepository.findById(dto.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setUpdatedAt(Instant.now());
        existing.setCategory(dto.getCategory());
        existing.setImagePath(dto.getImagePath());
        existing.setDescription(dto.getDescription());

        return productMapper.toDto(productRepository.save(existing));
    }

    public InputStream getProductImage(Long productId) {
        var entity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + "not found"));
        try {
            return minioService.downloadFile(entity.getImagePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to return product image");
        }
    }
}
