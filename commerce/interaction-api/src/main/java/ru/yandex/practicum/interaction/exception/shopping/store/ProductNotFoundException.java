package ru.yandex.practicum.interaction.exception.shopping.store;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

import java.util.UUID;

public class ProductNotFoundException extends BaseServiceException {
    public ProductNotFoundException() {
        this.httpStatus = "404";
        this.userMessage = "Ошибка, товар по идентификатору в БД не найден";
    }

    public ProductNotFoundException(Iterable<UUID> notFoundIds) {
        this.httpStatus = "404";
        this.userMessage = String.format("Ошибка, товары по идентификаторам %s в БД не найдены", notFoundIds);
    }
}
