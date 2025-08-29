package ru.yandex.practicum.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Размеры товара
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    // Ширина
    @NotNull
    @Min(1)
    BigDecimal width;

    // Высота
    @NotNull
    @Min(1)
    BigDecimal height;

    // Глубина
    @NotNull
    @Min(1)
    BigDecimal depth;
}
