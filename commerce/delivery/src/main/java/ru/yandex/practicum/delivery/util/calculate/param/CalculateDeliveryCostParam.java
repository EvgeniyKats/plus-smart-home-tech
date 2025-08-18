package ru.yandex.practicum.delivery.util.calculate.param;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.delivery.model.Address;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateDeliveryCostParam {
    @NotNull
    Address fromAddress;

    @NotNull
    Address toAddress;

    @NotNull
    Boolean fragile;

    @NotNull
    BigDecimal weight;

    @NotNull
    BigDecimal volume;
}
