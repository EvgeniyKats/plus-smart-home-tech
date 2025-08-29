package ru.yandex.practicum.payment.util.calculate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.payment.util.calculate.param.CalculateProductsCostParam;
import ru.yandex.practicum.payment.util.calculate.param.CalculateTotalCostParam;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentCalculateTest {
    PaymentCalculate paymentCalculate = new PaymentCalculateImpl();

    @Test
    void calculateProductsCost() {
        UUID itemId1 = UUID.randomUUID();
        Long quantityItem1 = 5L;
        BigDecimal costItem1 = new BigDecimal("1.0");

        UUID itemId2 = UUID.randomUUID();
        Long quantityItem2 = 1L;
        BigDecimal costItem2 = new BigDecimal("10.0");


        CalculateProductsCostParam param = CalculateProductsCostParam.builder()
                .productsPrice(Map.of(itemId1, costItem1, itemId2, costItem2))
                .productsQuantity(Map.of(itemId1, quantityItem1, itemId2, quantityItem2))
                .build();
        BigDecimal ans = paymentCalculate.calculateProductsCost(param);
        BigDecimal expected = new BigDecimal("15.00");
        assertEquals(expected, ans);
    }

    @Test
    void calculateTotalCost() {
        CalculateTotalCostParam param = CalculateTotalCostParam.builder()
                .productsCost(new BigDecimal("1000.0"))
                .deliveryCost(new BigDecimal("500.0"))
                .feePercent(new BigDecimal("10.0"))
                .build();

        BigDecimal total = paymentCalculate.calculateTotalCost(param);
        BigDecimal expected = new BigDecimal("1600.00");
        assertEquals(expected, total);
    }

    @Test
    void calculateFee() {
        BigDecimal price = new BigDecimal("1000.0");
        BigDecimal percentFee = new BigDecimal("10.0");

        BigDecimal fee = paymentCalculate.calculateFee(price, percentFee);
        BigDecimal expected = new BigDecimal("100.00");
        assertEquals(expected, fee);
    }
}