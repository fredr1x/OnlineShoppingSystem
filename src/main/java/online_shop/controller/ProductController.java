package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductCategoryDto;
import online_shop.dto.ProductDto;
import online_shop.dto.ProductStockDto;
import online_shop.service.ProductService;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/top_10")
    public ResponseEntity<List<ProductDto>> getTop10Products() {
        return ResponseEntity.ok().body(productService.getTop10Products());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable @Validated ProductCategoryDto category) {
        return ResponseEntity.ok().body(productService.getAllProductsByCategory(category));
    }

    @PostMapping("/add")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Validated(OnCreate.class) ProductDto product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(product));
    }

    @PutMapping("/change_stock")
    @PreAuthorize(value = "hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductStockDto> changeStock(@RequestBody @Validated(OnUpdate.class) ProductStockDto productStock) {
        var newProductStock = productService.changeStock(productStock);
        return ResponseEntity.ok().body(newProductStock);
    }
}
