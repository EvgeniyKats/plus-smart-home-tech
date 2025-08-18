package ru.yandex.practicum.interaction.exception.payment;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class NotEnoughInfoInOrderToCalculateException extends BaseServiceException {
    public NotEnoughInfoInOrderToCalculateException() {
        this.httpStatus = "400";
        this.userMessage = "Недостаточно информации в заказе для расчёта";
    }

    public NotEnoughInfoInOrderToCalculateException(String userMessage) {
        this.httpStatus = "400";
        this.userMessage = userMessage;
    }
}
