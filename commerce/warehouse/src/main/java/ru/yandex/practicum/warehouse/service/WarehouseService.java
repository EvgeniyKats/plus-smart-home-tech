package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    // Добавить новый товар на склад.
    void newProduct(NewProductInWarehouseRequest newRequest);

    // Предварительно проверить что количество товаров на складе достаточно для данной корзины товаров.
    BookedProductsDto checkProducts(ShoppingCartDto shoppingCartDto);

    // Принять товар на склад.
    void addProduct(AddProductToWarehouseRequest addRequest);

    // Передать товары в доставку
    void shipped(ShippedToDeliveryRequest shippedToDeliveryRequest);

    // Принять возврат товаров на склад.
    void returnProducts(Map<UUID, Long> products);

    // Собрать товары к заказу для подготовки к отправке.
    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);

    // Предоставить адрес склада для расчёта доставки.
    AddressDto getAddress();
}
