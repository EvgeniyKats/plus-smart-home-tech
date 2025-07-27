package ru.yandex.practicum.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Размеры товара
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    // Ширина
    @NotNull
    @Min(1)
    Double width;

    // Высота
    @NotNull
    @Min(1)
    Double height;

    // Глубина
    @NotNull
    @Min(1)
    Double depth;
}
