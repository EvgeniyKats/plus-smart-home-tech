package ru.yandex.practicum.interaction.client.feign.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
public class PaymentFallback implements PaymentClientFeign {
    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void success(UUID paymentId) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void failed(UUID paymentId) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void setStatusCancel(UUID paymentId) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void returnPayment(UUID paymentId) {
        PaymentFallbackException cause = new PaymentFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }
}
