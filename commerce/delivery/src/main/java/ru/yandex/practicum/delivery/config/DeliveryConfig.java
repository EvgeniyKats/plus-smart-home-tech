package ru.yandex.practicum.delivery.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "delivery.config")
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryConfig {
    DeliveryValues deliveryValues;

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    public static class DeliveryValues {
        BigDecimal baseCost;
        BigDecimal warehouseAddress1Multiply;
        BigDecimal warehouseAddress2Multiply;
        BigDecimal fragileMultiply;
        BigDecimal weightMultiply;
        BigDecimal volumeMultiply;
        BigDecimal differentStreetMultiply;
    }
}
