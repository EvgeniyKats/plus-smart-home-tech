package ru.yandex.practicum.interaction.exception.warehouse;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class ProductInShoppingCartLowQuantityInWarehouse extends BaseServiceException {

    public ProductInShoppingCartLowQuantityInWarehouse() {
        this.httpStatus = "400";
        this.userMessage = "Ошибка, товар из корзины не находится в требуемом количестве на складе";
    }
}
