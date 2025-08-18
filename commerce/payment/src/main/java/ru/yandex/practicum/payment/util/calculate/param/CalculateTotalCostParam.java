package ru.yandex.practicum.payment.util.calculate.param;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateTotalCostParam {
    @NotNull
    @Positive
    BigDecimal deliveryCost;

    @NotNull
    @Positive
    BigDecimal productsCost;

    @NotNull
    @Positive
    BigDecimal feePercent;
}
