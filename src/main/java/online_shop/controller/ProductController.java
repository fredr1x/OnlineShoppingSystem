package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductDto;
import online_shop.service.ProductService;
import online_shop.validation.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_SELLER')")
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Validated(OnCreate.class) ProductDto product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(product));
    }

}
