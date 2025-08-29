package ru.yandex.practicum.warehouse.service.param;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.warehouse.model.Product;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultCheckWarehouseProductsQuantity {
    // Отображение идентификатора на товар из БД
    Map<UUID, Product> products;

    // Общие сведения о товарах.
    BookedProductsDto bookedProductsDto;
}
