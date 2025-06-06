package online_shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import online_shop.validation.OnUpdate;
import org.hibernate.sql.Update;

@Data
@Builder
public class ProductStockDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "stock must be not null", groups = OnUpdate.class)
    private Integer stock;

}
