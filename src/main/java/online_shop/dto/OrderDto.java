package online_shop.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OrderDto {

    private Long id;

    private String orderStatus;

    private List<OrderProductDto> products;

}
