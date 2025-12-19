package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.ProductDto;
import online_shop.dto.ProductPriceDto;
import online_shop.dto.ProductStockDto;
import online_shop.exception.ProductNotFoundException;
import online_shop.service.ProductService;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Tag(
        name = "Products",
        description = "Управление товарами, поиск, фильтрация и администрирование"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Получить товар по ID",
            description = "Возвращает детальную информацию о товаре"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Товар найден",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар не найден"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(
            @Parameter(description = "ID товара", required = true, example = "10")
            @PathVariable Long id
    ) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(
            summary = "Получить изображение товара",
            description = "Возвращает изображение товара по его ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение успешно получено",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар или изображение не найдено"
            )
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getProductImage(
            @Parameter(description = "ID товара", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(new InputStreamResource(productService.getProductImage(id)));
    }

    @Operation(
            summary = "Топ-10 популярных товаров",
            description = "Возвращает список 10 самых популярных товаров"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список товаров получен",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
    )
    @GetMapping("/top_10")
    public ResponseEntity<List<ProductDto>> getTop10Products() {
        return ResponseEntity.ok(productService.getTop10Products());
    }

    @Operation(
            summary = "Получить товары по категории",
            description = "Возвращает список товаров выбранной категории"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Товары получены",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректная категория"
            )
    })
    @GetMapping("/category")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(
            @RequestParam String category
    ) {
        return ResponseEntity.ok(productService.getAllProductsByCategory(category));
    }

    @Operation(
            summary = "Поиск товаров по названию",
            description = "Ищет товары по ключевому слову"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Результаты поиска",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
    )
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductsByName(
            @Parameter(description = "Ключевое слово для поиска", example = "iphone")
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(productService.searchByKeyword(keyword));
    }

    @Operation(
            summary = "Фильтрация товаров",
            description = "Фильтрация по рейтингу, цене и категории"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Отфильтрованный список товаров",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
    )
    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProducts(
            @Parameter(description = "Минимальный рейтинг")
            @RequestParam(required = false) Float ratingAbove,

            @Parameter(description = "Минимальная цена")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Максимальная цена")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Категория товара")
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(productService.filter(ratingAbove, minPrice, maxPrice, category));
    }

    @Operation(
            summary = "Добавить новый товар",
            description = "Создаёт новый товар (доступно только модератору)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Товар успешно создан",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав"
            )
    })
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductDto> addProduct(
            @Parameter(description = "Данные товара")
            @RequestPart("product") @Validated(OnCreate.class) ProductDto product,

            @Parameter(description = "Изображение товара")
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(product, image));
    }

    @Operation(
            summary = "Обновить товар",
            description = "Полное обновление данных товара"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Товар обновлён",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар не найден"
            )
    })
    @PutMapping("/update_product")
    @PreAuthorize("hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductDto> updateProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновлённые данные товара",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            )
            @RequestBody @Validated(OnUpdate.class) ProductDto product
    ) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @Operation(
            summary = "Изменить остаток товара",
            description = "Обновляет количество товара на складе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Остаток обновлён",
            content = @Content(schema = @Schema(implementation = ProductStockDto.class))
    )
    @PatchMapping("/change_stock")
    @PreAuthorize("hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductStockDto> changeStock(
            @RequestBody @Validated(OnUpdate.class) ProductStockDto productStock
    ) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.changeStock(productStock));
    }

    @Operation(
            summary = "Изменить цену товара",
            description = "Обновляет цену товара"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Цена обновлена",
            content = @Content(schema = @Schema(implementation = ProductPriceDto.class))
    )
    @PatchMapping("/change_price")
    @PreAuthorize("hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<ProductPriceDto> changePrice(
            @RequestBody @Validated(OnUpdate.class) ProductPriceDto productPrice
    ) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.changePrice(productPrice));
    }

    @Operation(
            summary = "Удалить товар",
            description = "Удаляет товар по ID (только для модератора)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Товар удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар не найден"
            )
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID товара", required = true)
            @PathVariable("id") Long id
    ) throws ProductNotFoundException {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
