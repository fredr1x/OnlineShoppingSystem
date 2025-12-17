package online_shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import online_shop.dto.ReviewDeleteRequest;
import online_shop.dto.ReviewDto;
import online_shop.dto.ReviewWithRating;
import online_shop.exception.*;
import online_shop.service.ReviewService;
import online_shop.validation.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Reviews",
        description = "Отзывы и рейтинги товаров"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Получить отзывы по товару",
            description = "Возвращает отзывы и общий рейтинг товара. " +
                          "Можно отфильтровать по положительным/отрицательным отзывам."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Отзывы успешно получены",
                    content = @Content(schema = @Schema(implementation = ReviewWithRating.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Товар не найден"
            )
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ReviewWithRating> getReviews(
            @Parameter(description = "ID товара", required = true, example = "5")
            @PathVariable("productId") Long productId,

            @Parameter(
                    description = "Фильтр по положительным отзывам. " +
                                  "true — только положительные, false — только отрицательные"
            )
            @RequestParam(required = false) Boolean positiveFeedbacks
    ) throws ProductNotFoundException {

        return ResponseEntity.ok(
                reviewService.getAllReviewsByProductId(productId, positiveFeedbacks)
        );
    }

    @Operation(
            summary = "Оставить отзыв",
            description = "Создаёт новый отзыв пользователя на товар"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Отзыв успешно создан",
                    content = @Content(schema = @Schema(implementation = ReviewWithRating.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный отзыв"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь или товар не найден"
            )
    })
    @PostMapping("/make_review")
    public ResponseEntity<ReviewWithRating> makeReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные отзыва",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReviewDto.class))
            )
            @RequestBody @Validated(OnCreate.class) ReviewDto reviewDto
    ) throws UserNotFoundException, ProductNotFoundException, IllegalReviewException {

        return ResponseEntity.ok(reviewService.makeReview(reviewDto));
    }

    @Operation(
            summary = "Удалить отзыв",
            description = "Удаляет отзыв пользователя по переданным данным"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Отзыв успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав для удаления отзыва"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отзыв не найден"
            )
    })
    @DeleteMapping("/delete_review")
    public ResponseEntity<Void> deleteReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для удаления отзыва",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReviewDeleteRequest.class))
            )
            @RequestBody @Validated ReviewDeleteRequest reviewDto
    ) throws ReviewNotFoundException, IllegalReviewException, IdMismatchException {

        reviewService.deleteReview(reviewDto);
        return ResponseEntity.noContent().build();
    }
}
