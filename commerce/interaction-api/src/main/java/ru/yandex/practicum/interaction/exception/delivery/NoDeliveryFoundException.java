package ru.yandex.practicum.interaction.exception.delivery;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class NoDeliveryFoundException extends BaseServiceException {
    public NoDeliveryFoundException() {
        this.httpStatus = "404";
        this.userMessage = "Не найдена доставка для расчёта";
    }
}
