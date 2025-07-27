package ru.yandex.practicum.interaction.dto.shopping.store;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Запрос на изменение статуса остатка товара
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequest {
    // Идентификатор товара
    @NotNull
    UUID productId;

    // Статус, перечисляющий состояние остатка как свойства товара
    @NotNull
    QuantityState quantityState;
}
