package ru.yandex.practicum.shopping.cart.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    // Получить актуальную корзину для авторизованного пользователя.
    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        log.info("start получить корзину username={}", username);
        // TODO реализовать
        return null;
    }

    // Добавить товар в корзину
    @PutMapping
    public ShoppingCartDto addProductsToShoppingCart(
            @RequestBody @NotEmpty Map<UUID, @NotNull Integer> products, // Отображение идентификатора товара на отобранное количество
            @RequestParam String username) {
        log.info("start добавить товары в корзину username={}, products={}", username, products);
        // TODO реализовать
        return null;
    }

    // Деактивация корзины товаров для пользователя
    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam String username) {
        log.info("start деактивировать корзину username={}", username);
        // TODO реализовать
    }

    // Удалить указанные товары из корзины пользователя
    @PostMapping("/remove")
    public ShoppingCartDto removeProductsFromShoppingCart(
            @RequestBody @NotEmpty List<UUID> productsIds, // Список идентификаторов товаров, которые нужно удалить
            @RequestParam String username) {
        log.info("start удалить товары username={}, productsIds={}", username, productsIds);
        // TODO реализовать
        return null;
    }

    // Изменить количество товаров в корзине
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductsQuantityInShoppingCart(
            @RequestBody @NotEmpty Map<UUID, @NotNull Integer> products, // Отображение идентификатора товара на отобранное количество
            @RequestParam String username) {
        log.info("start изменить количество товаров username={}, products={}", username, products);
        // TODO реализовать
        return null;
    }
}
