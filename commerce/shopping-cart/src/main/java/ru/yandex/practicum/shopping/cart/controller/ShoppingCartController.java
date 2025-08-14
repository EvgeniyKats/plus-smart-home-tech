package ru.yandex.practicum.shopping.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.shopping.cart.ShoppingCartApi;
import ru.yandex.practicum.interaction.dto.shopping.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.shopping.cart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartApi {
    private final ShoppingCartService shoppingCartService;

    // Получить актуальную корзину для авторизованного пользователя.
    @Logging
    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    // Добавить товар в корзину
    @Logging
    @Override
    public ShoppingCartDto addProductsToShoppingCart(
            Map<UUID, Integer> products, // Отображение идентификатора товара на отобранное количество
            String username) {
        return shoppingCartService.addProductsToShoppingCart(products, username);
    }

    // Деактивация корзины товаров для пользователя
    @Logging
    @Override
    public void deactivateShoppingCart(String username) {
        shoppingCartService.deactivateShoppingCart(username);
    }

    // Удалить указанные товары из корзины пользователя
    @Logging
    @Override
    public ShoppingCartDto removeProductsFromShoppingCart(
            List<UUID> productsIds, // Список идентификаторов товаров, которые нужно удалить
            String username) {
        return shoppingCartService.removeProductsFromShoppingCart(productsIds, username);
    }

    // Изменить количество товаров в корзине
    @Logging
    @Override
    public ShoppingCartDto changeProductsQuantityInShoppingCart(
            ChangeProductQuantityRequest request, // Отображение идентификатора товара на отобранное количество
            String username) {
        return shoppingCartService.changeProductsQuantityInShoppingCart(request, username);
    }
}
