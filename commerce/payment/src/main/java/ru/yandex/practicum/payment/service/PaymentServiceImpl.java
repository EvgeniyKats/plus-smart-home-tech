package ru.yandex.practicum.payment.service;

import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.logging.Logging;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    @Logging(Level.TRACE)
    public PaymentDto createPayment(OrderDto orderDto) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public BigDecimal getTotalCost(OrderDto orderDto) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public void setPaymentSuccess(UUID paymentId) {

    }

    @Override
    @Logging(Level.TRACE)
    public BigDecimal getProductCost(OrderDto orderDto) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public void setPaymentFailed(UUID paymentId) {

    }
}
