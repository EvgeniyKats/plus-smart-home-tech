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

    // Формирование оплаты для заказа (переход в платежный шлюз).
    @Override
    @Logging
    public PaymentDto createPayment(OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    // Расчёт полной стоимости заказа.
    @Override
    @Logging
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        return paymentService.calculateTotalCost(orderDto);
    }

    // Метод для эмуляции успешной оплаты в платежном шлюзе.
    @Override
    @Logging
    public void success(UUID paymentId) {
        paymentService.success(paymentId);
    }

    // Расчёт стоимости товаров в заказе.
    @Override
    @Logging
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    // Метод для эмуляции отказа в оплате платежного шлюза.
    @Override
    @Logging
    public void failed(UUID paymentId) {
        paymentService.failed(paymentId);
    }

    @Override
    @Logging
    public void setStatusCancel(UUID paymentId) {
        paymentService.setStatusCancel(paymentId);
    }

    @Override
    @Logging
    public void returnPayment(UUID paymentId) {
        paymentService.returnPayment(paymentId);
    }
}
