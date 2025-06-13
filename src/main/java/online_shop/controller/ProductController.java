package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductCategoryDto;
import online_shop.dto.ProductDto;
import online_shop.dto.ProductPriceDto;
import online_shop.dto.ProductStockDto;
import online_shop.exception.ProductNotFoundException;
import online_shop.service.ProductService;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @GetMapping("/top_10")
    public ResponseEntity<List<ProductDto>> getTop10Products() {
        return ResponseEntity.ok().body(productService.getTop10Products());
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@RequestBody @Validated ProductCategoryDto category) {
        return ResponseEntity.ok().body(productService.getAllProductsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductsByName(@RequestParam String keyword) {
        return ResponseEntity.ok().body(productService.searchByKeyword(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProducts(@RequestParam(required = false) Float ratingAbove,
                                                           @RequestParam(required = false) BigDecimal minPrice,
                                                           @RequestParam(required = false) BigDecimal maxPrice,
                                                           @RequestParam(required = false) ProductCategoryDto category) {


        return ResponseEntity
                .ok()
                .body(productService.filter(ratingAbove, minPrice, maxPrice, category));

    }

                                                           @PostMapping("/add")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Validated(OnCreate.class) ProductDto product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(product));
    }

    @PutMapping("/update_product")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Validated(OnUpdate.class) ProductDto product) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.updateProduct(product));
    }

    @PatchMapping("/change_stock")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductStockDto> changeStock(@RequestBody @Validated(OnUpdate.class) ProductStockDto productStock) throws ProductNotFoundException {
        return ResponseEntity
                .ok()
                .body(productService.changeStock(productStock));
    }

    @PatchMapping("/change_price")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductPriceDto> changePrice(@RequestBody @Validated(OnUpdate.class) ProductPriceDto productPrice) throws ProductNotFoundException {
        return ResponseEntity
                .ok()
                .body(productService.changePrice(productPrice));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
