package ru.yandex.practicum.payment.util.calculate.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateProductsCostParam {
    @NotEmpty
    Map<UUID, @Positive Long> productsQuantity;

    @NotEmpty
    Map<UUID, @Positive BigDecimal> productsPrice;
}
