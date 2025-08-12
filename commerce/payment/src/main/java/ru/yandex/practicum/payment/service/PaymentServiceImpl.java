package ru.yandex.practicum.payment.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        return null;
    }

    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        return null;
    }

    @Override
    public void setPaymentSuccess(UUID paymentId) {

    }

    @Override
    public BigDecimal getProductCost(OrderDto orderDto) {
        return null;
    }

    @Override
    public void setPaymentFailed(UUID paymentId) {

    }
}
