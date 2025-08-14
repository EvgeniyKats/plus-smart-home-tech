package ru.yandex.practicum.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.payment.PaymentApi;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@Validated
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {
    private final PaymentService paymentService;

    @Override
    @Logging
    public PaymentDto createPayment(OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    @Override
    @Logging
    public BigDecimal getTotalCost(OrderDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }

    @Override
    @Logging
    public void setPaymentSuccess(UUID paymentId) {
        paymentService.setPaymentSuccess(paymentId);
    }

    @Override
    @Logging
    public BigDecimal getProductCost(OrderDto orderDto) {
        return paymentService.getProductCost(orderDto);
    }

    @Override
    @Logging
    public void setPaymentFailed(UUID paymentId) {
        paymentService.setPaymentFailed(paymentId);
    }
}
