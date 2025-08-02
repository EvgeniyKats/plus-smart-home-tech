package ru.yandex.practicum.interaction.client.feign.shopping.cart;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart", fallback = ShoppingCartFallback.class)
public interface ShoppingCartClientFeign {

    @GetMapping
    ShoppingCartDto getShoppingCart(String username);

    // Добавить товар в корзину
    @PutMapping
    ShoppingCartDto addProductsToShoppingCart(
            Map<UUID, Integer> products, // Отображение идентификатора товара на отобранное количество
            String username);

    // Деактивация корзины товаров для пользователя
    @DeleteMapping
    void deactivateShoppingCart(String username);

    // Удалить указанные товары из корзины пользователя
    @PostMapping("/remove")
    ShoppingCartDto removeProductsFromShoppingCart(
            List<UUID> productsIds, // Список идентификаторов товаров, которые нужно удалить
            String username);

    // Изменить количество товаров в корзине
    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductsQuantityInShoppingCart(
            Map<UUID, Integer> products, // Отображение идентификатора товара на отобранное количество
            String username);
}
