package ru.yandex.practicum.interaction.exception.warehouse;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

import java.util.List;
import java.util.UUID;

public class ProductInShoppingCartLowQuantityInWarehouse extends BaseServiceException {

    public ProductInShoppingCartLowQuantityInWarehouse(List<UUID> notFoundIds) {
        this.httpStatus = "400";
        this.userMessage = "Ошибка, товар из корзины не находится в требуемом количестве на складе IDs: " + notFoundIds;
    }
}
