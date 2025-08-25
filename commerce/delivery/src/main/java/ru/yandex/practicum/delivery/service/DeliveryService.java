package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    // Создать новую доставку в БД.
    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    // Расчёт полной стоимости доставки заказа.
    BigDecimal calculateDeliveryCost(OrderDto orderDto);

    // Эмуляция получения товара в доставку.
    void picked(UUID deliveryId);

    // Эмуляция успешной доставки товара.
    void success(UUID deliveryId);

    // Эмуляция неудачной доставки, например товар не вручен или не может быть доставлен.
    void failed(UUID deliveryId);

    // Эмуляция отмены доставки товаров.
    void setStatusCanceled(UUID deliveryId);

    // Эмуляция успешной доставки товара до пункта выдачи.
    void onPickup(UUID deliveryId);
}
