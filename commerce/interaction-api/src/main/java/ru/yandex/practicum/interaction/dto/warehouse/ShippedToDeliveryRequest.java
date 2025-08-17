package ru.yandex.practicum.interaction.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Запрос на передачу в доставку товаров.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippedToDeliveryRequest {
    // Идентификатор заказа в БД
    @NotNull
    UUID orderId;

    // Идентификатор доставки в БД
    @NotNull
    UUID deliveryId;
}
