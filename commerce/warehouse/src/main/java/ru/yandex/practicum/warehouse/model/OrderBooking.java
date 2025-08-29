package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Забронированные товары, которые были собраны для заказа
 */

// JPA annotations
@Entity
@Table(name = "booked_product")
// Lombok annotations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBooking {
    // идентификатор заказа
    @Id
    UUID orderId;

    // идентификатор доставки
    @Column(name = "delivery_id")
    UUID deliveryId;

    // отображение идентификатора товара на его количество
    @ElementCollection
    @CollectionTable(
            name = "booked_products_items",
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity", nullable = false)
    @Builder.Default
    Map<UUID, Long> bookedProducts = new HashMap<>();
}
