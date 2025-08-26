package ru.yandex.practicum.interaction.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Представление заказа в системе.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    // Идентификатор заказа.
    @NotNull
    UUID orderId;

    // Идентификатор корзины.
    UUID shoppingCartId;

    // Отображение идентификатора товара на отобранное количество.
    @NotNull
    Map<UUID, @Positive Long> products;

    // Идентификатор оплаты.
    UUID paymentId;

    // Идентификатор доставки.
    UUID deliveryId;

    // Статус заказа.
    OrderState state;

    // Общий вес доставки.
    BigDecimal deliveryWeight;

    // Общий объём доставки.
    BigDecimal deliveryVolume;

    // Признак хрупкости заказа.
    Boolean fragile;

    // Общая стоимость.
    BigDecimal totalPrice;

    // Стоимость доставки.
    BigDecimal deliveryPrice;

    // Стоимость товаров в заказе.
    BigDecimal productPrice;
}