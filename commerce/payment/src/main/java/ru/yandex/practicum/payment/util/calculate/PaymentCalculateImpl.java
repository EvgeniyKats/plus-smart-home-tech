package ru.yandex.practicum.payment.util.calculate;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.payment.util.calculate.param.CalculateProductsCostParam;
import ru.yandex.practicum.payment.util.calculate.param.CalculateTotalCostParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
@Validated
public class PaymentCalculateImpl implements PaymentCalculate {
    // необходим для приведения налога к итоговому значению
    private static final BigDecimal FEE_DIVIDER = new BigDecimal("100");

    @Override
    public BigDecimal calculateProductsCost(CalculateProductsCostParam param) {
        return param.getProductsQuantity().entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    Long quantity = entry.getValue();
                    BigDecimal price = param.getProductsPrice().get(productId);
                    if (price == null) {
                        throw new IllegalArgumentException(
                                String.format("Нет данных о стоимости товара id: %s", productId));
                    }
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal calculateTotalCost(CalculateTotalCostParam param) {
        BigDecimal fee = calculateFee(param.getProductsCost(), param.getFeePercent());
        return param.getProductsCost()
                .add(fee)
                .add(param.getDeliveryCost())
                .setScale(2, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal calculateFee(BigDecimal productsCost, BigDecimal feePercent) {
        return productsCost.multiply(feePercent).divide(FEE_DIVIDER, 2, RoundingMode.DOWN);
    }
}
