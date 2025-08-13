package ru.yandex.practicum.order.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.dto.order.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Представление заказа в БД
 */

// lombok annotations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// JPA annotations
@Entity
@Table(name = "order")
public class Order {

    // Идентификатор заказа.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    UUID orderId;

    // Идентификатор корзины.
    @Column(name = "shopping_cart_id", nullable = false)
    UUID shoppingCartId;

    // Идентификатор оплаты.
    @Column(name = "payment_id", nullable = false)
    UUID paymentId;

    @Embedded
    OrderDelivery delivery;

    @Embedded
    OrderProducts products;

    // Общая стоимость.
    @Column(name = "total_price", precision = 19, scale = 2, nullable = false)
    BigDecimal totalPrice;

    // Статус заказа.
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false)
    OrderState state;
}
