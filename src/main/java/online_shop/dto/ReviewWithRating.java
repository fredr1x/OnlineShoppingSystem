package online_shop.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewWithRating {

    private Long productId;

    @Min(value = 0)
    private Float overallRating;

    private Integer numberOfReviews;

    private List<ReviewDto> reviews;

}
