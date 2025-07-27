package ru.yandex.practicum.interaction.dto.shopping.cart;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Запрос на изменение количества единиц товара
 */

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProductQuantityRequest {
    // Идентификатор товара
    @NotNull
    UUID productId;

    // Новое количество товара
    @NotNull
    Integer newQuantity;
}
