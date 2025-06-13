package online_shop.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping("/{productId}")
    public ResponseEntity<ReviewWithRating> getReviews(@PathVariable("productId") Long productId,
                                                       @RequestParam(required = false) Boolean positiveFeedbacks) throws ProductNotFoundException {

        return ResponseEntity.ok().body(reviewService.getAllReviewsByProductId(productId, positiveFeedbacks));
    }

    @PostMapping("/make_review")
    public ResponseEntity<ReviewWithRating> makeReview(@RequestBody @Validated(OnCreate.class) ReviewDto reviewDto) throws UserNotFoundException, ProductNotFoundException, IllegalReviewException {
        return ResponseEntity.ok().body(reviewService.makeReview(reviewDto));
    }

    @DeleteMapping("/delete_review")
    public ResponseEntity<Void> deleteReview(@RequestBody @Validated ReviewDeleteRequest reviewDto) throws ReviewNotFoundException, IllegalReviewException, IdMismatchException {
        reviewService.deleteReview(reviewDto);
        return ResponseEntity.noContent().build();
    }
}
