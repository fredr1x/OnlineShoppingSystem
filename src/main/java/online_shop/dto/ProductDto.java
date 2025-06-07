package online_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online_shop.entity.enums.Category;
import online_shop.validation.OnCreate;
import online_shop.validation.OnUpdate;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "category must be not null", groups = {OnUpdate.class, OnCreate.class})
    private Category category;

    @NotNull(message = "name must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @NotNull(message = "description must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String description;

    @NotNull(message = "price must be not null", groups = {OnUpdate.class, OnCreate.class})
    private BigDecimal price;

    @NotNull(message = "stock must be not null", groups = {OnUpdate.class, OnCreate.class})
    private Integer stock;

    @JsonProperty(value = "image_path")
    @NotNull(message = "image path must be not null", groups = {OnCreate.class})
    private String imagePath;

    @Nullable()
    private Integer rating;
}
