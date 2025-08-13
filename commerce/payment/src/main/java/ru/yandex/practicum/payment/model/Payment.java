package ru.yandex.practicum.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сформированная оплата заказа в БД.
 */

// lombok annotations
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// JPA annotations
@Entity
@Table(name = "payment")
public class Payment {

    // Идентификатор оплаты.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID paymentId;

    // Общая стоимость.
    @Column(name = "total", scale = 2, precision = 19, nullable = false)
    BigDecimal totalPayment;

    // Стоимость доставки.
    @Column(name = "delivery", scale = 2, precision = 19, nullable = false)
    BigDecimal deliveryTotal;

    // Стоимость налога.
    @Column(name = "fee", scale = 2, precision = 19, nullable = false)
    BigDecimal feeTotal;
}
