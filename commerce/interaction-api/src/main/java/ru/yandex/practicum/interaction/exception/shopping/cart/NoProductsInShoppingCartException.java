package ru.yandex.practicum.interaction.exception.shopping.cart;

import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class NoProductsInShoppingCartException extends BaseServiceException {

    public NoProductsInShoppingCartException(Throwable cause) {
        super(cause);
        this.httpStatus = "400";
        this.userMessage = "Нет искомых товаров в корзине";
    }
}
