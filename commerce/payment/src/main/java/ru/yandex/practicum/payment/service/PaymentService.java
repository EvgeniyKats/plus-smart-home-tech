package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    // Формирование оплаты для заказа (переход в платежный шлюз).
    PaymentDto createPayment(OrderDto orderDto);

    // Расчёт полной стоимости заказа.
    BigDecimal calculateTotalCost(OrderDto orderDto);

    // Метод для эмуляции успешной оплаты в платежном шлюзе.
    void success(UUID paymentId);

    // Расчёт стоимости товаров в заказе.
    BigDecimal calculateProductCost(OrderDto orderDto);

    // Метод для эмуляции отказа в оплате платежного шлюза.
    void failed(UUID paymentId);

    // Метод для эмуляции отмены заявки на оплату.
    void setStatusCancel(UUID paymentId);

    // Метод для эмуляции возврата оплаты.
    void returnPayment(UUID paymentId);
}
