package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.PaymentDto;
import online_shop.mapper.PaymentMapper;
import online_shop.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    public List<PaymentDto> getAllPaymentsByUserId(Long userId) {
        return paymentMapper.toDto(paymentRepository.findAllByUserId(userId));
    }
}
