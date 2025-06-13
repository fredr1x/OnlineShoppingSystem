package online_shop.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import online_shop.dto.ReviewDeleteRequest;
import online_shop.dto.ReviewDto;
import online_shop.dto.ReviewWithRating;
import online_shop.entity.Product;
import online_shop.entity.Review;
import online_shop.entity.User;
import online_shop.exception.*;
import online_shop.mapper.ReviewMapper;
import online_shop.repository.OrderRepository;
import online_shop.repository.ProductRepository;
import online_shop.repository.ReviewRepository;
import online_shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ReviewWithRating getAllReviewsByProductId(Long productId, Boolean positiveFeedbacks) throws ProductNotFoundException{

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " not found"));

        List<Review> reviews = reviewRepository.findAll((root, query, cb) ->{

            List<Predicate> predicates = new ArrayList<>();

            if (positiveFeedbacks != null) {

                if (positiveFeedbacks) {
                    predicates.add(cb.greaterThan(root.get("rating"), 2));
                } else {
                    predicates.add(cb.lessThanOrEqualTo(root.get("rating"), 2));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });

        return ReviewWithRating.builder()
                .productId(product.getId())
                .overallRating(reviewRepository.calculateRating(product.getId()))
                .reviews(reviewMapper.toDto(reviews))
                .numberOfReviews(reviews.size())
                .build();
    }

    @Transactional
    public ReviewWithRating makeReview(ReviewDto reviewDto) throws UserNotFoundException, ProductNotFoundException, IllegalReviewException {

        var user = getUser(reviewDto.getUserId());

        var product = getProduct(reviewDto.getProductId());

        if (!hasUserPurchasedProduct(user.getId(), product.getId())) {
            throw new IllegalReviewException("User has not purchased product, or user has no experience with product");
        }

        var review = Review.builder()
                .user(user)
                .product(product)
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .createdAt(Instant.now())
                .build();

        reviewRepository.save(review);

        product.setRating(calculateRating(product.getId()));
        var saved = productRepository.save(product);

        return ReviewWithRating.builder()
                .productId(saved.getId())
                .overallRating(saved.getRating())
                .reviews(getReviews(saved.getId()))
                .build();
    }

    @Transactional
    public void deleteReview(ReviewDeleteRequest reviewDto) throws ReviewNotFoundException, IdMismatchException, IllegalReviewException {

        var review = reviewRepository.findById(reviewDto.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: " + reviewDto.getReviewId() + " not found"));

        var user = review.getUser();

        var product = review.getProduct();


        if (!hasUserPurchasedProduct(user.getId(), product.getId())) {
            throw new IllegalReviewException("User must have ordered product to leave review");
        }

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new IdMismatchException("ID's must match");
        }

        reviewRepository.delete(review);
    }

    private boolean hasUserPurchasedProduct(Long userId, Long productId) {
        return orderRepository.hasUserPurchasedProduct(userId, productId);
    }

    private Float calculateRating(Long productId) {
        return reviewRepository.calculateRating(productId);
    }

    private Product getProduct(Long productId) throws ProductNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("product with id: " + productId + " not found"));
    }

    private User getUser(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user with id: " + userId + " not found"));
    }

    private List<ReviewDto> getReviews(Long productId) {
        return reviewMapper.toDto(reviewRepository.findReviewsByProductId(productId));
    }


}
