package online_shop.repository;

import online_shop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    @Query("select avg(r.rating) from Review r where r.product.id=:productId")
    Float calculateRating(@Param(value = "productId") Long productId);

    List<Review> findReviewsByProductId(Long productId);

}
