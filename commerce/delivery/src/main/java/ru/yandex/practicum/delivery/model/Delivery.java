package ru.yandex.practicum.delivery.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;

import java.util.UUID;

/**
 * Представление доставки в БД
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
@Table(name = "delivery")
public class Delivery {

    // Идентификатор доставки.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    UUID deliveryId;

    // Адрес отправителя (склад).
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "from_address_id", referencedColumnName = "id", nullable = false)
    Address fromAddress;

    // Адрес получателя (пользователя).
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "to_address_id", referencedColumnName = "id", nullable = false)
    Address toAddress;

    // Идентификатор заказа.
    @Column(name = "order_id", nullable = false)
    UUID orderId;

    // Статус доставки.
    @Column(name = "delivery_state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    DeliveryState deliveryState;
}
