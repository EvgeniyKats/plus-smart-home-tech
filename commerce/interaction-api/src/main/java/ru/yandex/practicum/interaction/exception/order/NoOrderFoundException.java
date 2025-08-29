package ru.yandex.practicum.interaction.exception.order;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class NoOrderFoundException extends BaseServiceException {
    public NoOrderFoundException() {
        this.httpStatus = "404";
        this.userMessage = "Не найден заказ";
    }
}
