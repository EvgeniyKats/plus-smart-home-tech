package ru.yandex.practicum.payment.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentState;
import ru.yandex.practicum.payment.model.Payment;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    @Test
    void shouldMapAllFieldsByPayment() {
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .totalPayment(new BigDecimal("7.0"))
                .deliveryTotal(new BigDecimal("5.0"))
                .feeTotal(new BigDecimal("1.5"))
                .paymentState(PaymentState.PENDING)
                .build();

        PaymentDto paymentDto = paymentMapper.toPaymentDto(payment);

        assertEquals(payment.getPaymentId(), paymentDto.getPaymentId());
        assertEquals(payment.getTotalPayment(), paymentDto.getTotalPayment());
        assertEquals(payment.getDeliveryTotal(), paymentDto.getDeliveryTotal());
        assertEquals(payment.getFeeTotal(), paymentDto.getFeeTotal());
    }
}