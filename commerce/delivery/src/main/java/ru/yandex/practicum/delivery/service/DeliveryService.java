package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    // Создать новую доставку в БД.
    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    // Расчёт полной стоимости доставки заказа.
    BigDecimal getCost(OrderDto orderDto);

    // Эмуляция получения товара в доставку.
    void picked(UUID orderId);

    // Эмуляция успешной доставки товара.
    void setSuccess(UUID orderId);

    // Эмуляция неудачного вручения товара.
    void setFailed(UUID orderId);
}
