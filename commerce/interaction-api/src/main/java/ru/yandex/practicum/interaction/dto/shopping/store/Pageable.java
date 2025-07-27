package ru.yandex.practicum.interaction.dto.shopping.store;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pageable {
    @Min(0)
    Integer page;

    @Min(1)
    Integer size;

    List<String> sort;
}
