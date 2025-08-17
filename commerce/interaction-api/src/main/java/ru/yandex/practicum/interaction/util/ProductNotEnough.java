package ru.yandex.practicum.interaction.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Информация о товаре, которого не хватает на складе, передаётся в ProductInShoppingCartLowQuantityInWarehouse
 */

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductNotEnough {
    // идентификатор товара
    UUID productId;

    // доступное количество для заказа
    Long availableCount;

    // ожидаемое количество для заказа
    Long wantedCount;

    // сколько товаров не хватает на складе
    Long differenceCount;

    public ProductNotEnough(UUID productId, Long availableCount, Long wantedCount) {
        this.productId = productId;
        this.availableCount = availableCount;
        this.wantedCount = wantedCount;
        this.differenceCount = wantedCount - availableCount;
    }
}
