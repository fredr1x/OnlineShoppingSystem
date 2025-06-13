package online_shop.mapper;

import online_shop.dto.PaymentDto;
import online_shop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "orderId", target = "order.id")
    Payment toEntity(PaymentDto paymentDto);
    List<Payment> toEntity(List<PaymentDto> paymentDtoList);

    @Mapping(source = "order.id", target = "orderId")
    PaymentDto toDto(Payment payment);
    List<PaymentDto> toDto(List<Payment> paymentList);

}
