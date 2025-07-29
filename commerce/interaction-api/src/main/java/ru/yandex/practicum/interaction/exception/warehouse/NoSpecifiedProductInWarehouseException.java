package ru.yandex.practicum.interaction.exception.warehouse;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class NoSpecifiedProductInWarehouseException extends BaseServiceException {
    public NoSpecifiedProductInWarehouseException() {
        this.httpStatus = "400";
        this.userMessage = "Нет информации о товаре на складе";
    }
}
