package ru.yandex.practicum.payment.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.feign.order.OrderClientFeign;
import ru.yandex.practicum.interaction.client.feign.shopping.store.ShoppingStoreClientFeign;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentState;
import ru.yandex.practicum.payment.config.PaymentConfig;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;
import ru.yandex.practicum.payment.util.calculate.PaymentCalculate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @MockBean
    ShoppingStoreClientFeign shoppingStoreClientFeign;

    @MockBean
    OrderClientFeign orderClientFeign;

    @MockBean
    PaymentConfig paymentConfig;

    @MockBean
    PaymentCalculate paymentCalculate;

    @Test
    void createPayment() {
        OrderDto orderDto = createTestOrderDto();
        PaymentDto paymentDto = paymentService.createPayment(orderDto);
        assertNotNull(paymentDto);
        assertNotNull(paymentDto.getPaymentId());
        when(paymentCalculate.calculateFee(any(), any()))
                .thenReturn(new BigDecimal("10.0"));

        Payment payment = paymentRepository.findById(paymentDto.getPaymentId()).orElse(null);

        assertNotNull(payment);
        Mockito.verify(paymentCalculate, times(1))
                .calculateFee(any(), any());
    }

    @Test
    void calculateTotalCost() {
        OrderDto orderDto = createTestOrderDto();
        paymentService.calculateTotalCost(orderDto);
        Mockito.verify(paymentCalculate, times(1))
                .calculateTotalCost(any());
    }

    @Test
    void calculateProductCost() {
        OrderDto orderDto = createTestOrderDto();
        paymentService.calculateProductCost(orderDto);
        Mockito.verify(paymentCalculate, times(1))
                .calculateProductsCost(any());
    }

    @Test
    void success() {
        Payment payment = createTestPayment();

        paymentService.success(payment.getPaymentId());
        Mockito.verify(orderClientFeign, times(1))
                .setStatusPaymentSuccess(payment.getOrderId());
        assertEquals(PaymentState.SUCCESS, payment.getPaymentState());
    }

    @Test
    void failed() {
        Payment payment = createTestPayment();

        paymentService.failed(payment.getPaymentId());
        Mockito.verify(orderClientFeign, times(1))
                .setStatusPaymentFailed(payment.getOrderId());
        assertEquals(PaymentState.FAILED, payment.getPaymentState());
    }

    @Test
    void setStatusCancel() {
        Payment payment = createTestPayment();
        paymentService.setStatusCancel(payment.getPaymentId());
        assertEquals(PaymentState.CANCELED, payment.getPaymentState());
    }

    @Test
    void returnPayment() {
        Payment payment = createTestPayment();
        payment.setPaymentState(PaymentState.SUCCESS);
        paymentService.returnPayment(payment.getPaymentId());
        assertEquals(PaymentState.RETURNED, payment.getPaymentState());
    }

    private OrderDto createTestOrderDto() {
        return OrderDto.builder()
                .orderId(UUID.randomUUID())
                .deliveryId(UUID.randomUUID())
                .shoppingCartId(UUID.randomUUID())
                .fragile(true)
                .deliveryPrice(new BigDecimal("10.0"))
                .deliveryWeight(new BigDecimal("11.0"))
                .deliveryVolume(new BigDecimal("15.0"))
                .productPrice(new BigDecimal("1000.0"))
                .totalPrice(new BigDecimal("2000.0"))
                .products(Map.of(UUID.randomUUID(), 1L))
                .state(OrderState.NEW)
                .build();
    }

    private Payment createTestPayment() {
        Payment payment = Payment.builder()
                .orderId(UUID.randomUUID())
                .totalPayment(new BigDecimal("5.0"))
                .deliveryTotal(new BigDecimal("1.0"))
                .feeTotal(new BigDecimal("1.0"))
                .build();
        paymentRepository.save(payment);
        return payment;
    }
}