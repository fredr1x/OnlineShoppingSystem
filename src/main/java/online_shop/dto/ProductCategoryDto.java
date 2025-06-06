package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCategoryDto {

    @NotNull(message = "category must be not null")
    private String category;

}
