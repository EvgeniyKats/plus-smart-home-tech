package ru.yandex.practicum.interaction.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;

/**
 * Запрос на создание заказа.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    // Корзина товаров в онлайн магазине.
    @NotNull
    @Valid
    ShoppingCartDto shoppingCart;

    // Представление адреса в системе.
    @NotNull
    @Valid
    AddressDto deliveryAddress;
}