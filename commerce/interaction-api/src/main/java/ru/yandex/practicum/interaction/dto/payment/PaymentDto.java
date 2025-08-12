package ru.yandex.practicum.interaction.dto.payment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сформированная оплата заказа.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {

    // Идентификатор оплаты.
    UUID paymentId;

    // Общая стоимость.
    BigDecimal totalPayment;

    // Стоимость доставки.
    BigDecimal deliveryTotal;

    // Стоимость налога.
    BigDecimal feeTotal;
}
