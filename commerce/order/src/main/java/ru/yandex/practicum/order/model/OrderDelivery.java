package ru.yandex.practicum.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Представление заказа в БД, данные доставки
 */

// lombok annotations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// JPA annotations
@Embeddable
public class OrderDelivery {

    // Идентификатор доставки.
    @Column(name = "delivery_id", nullable = false)
    UUID deliveryId;

    // Общий вес доставки.
    @Column(name = "delivery_weight", nullable = false)
    Double deliveryWeight;

    // Общий объём доставки.
    @Column(name = "delivery_volume", nullable = false)
    Double deliveryVolume;

    // Признак хрупкости заказа.
    @Column(name = "fragile", nullable = false)
    Boolean fragile;

    // Стоимость доставки.
    @Column(name = "delivery_price", precision = 19, scale = 2, nullable = false)
    BigDecimal deliveryPrice;
}
