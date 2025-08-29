package ru.yandex.practicum.interaction.exception.warehouse;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

import java.util.UUID;

public class OrderBookingNotFoundException extends BaseServiceException {
    public OrderBookingNotFoundException(UUID orderId) {
        this.httpStatus = "404";
        this.userMessage = String.format("Не найден забронированный заказ с orderId %s", orderId);
    }
}
