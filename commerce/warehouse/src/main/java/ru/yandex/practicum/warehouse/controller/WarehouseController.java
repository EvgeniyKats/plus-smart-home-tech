package ru.yandex.practicum.warehouse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.warehouse.WarehouseApi;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@Validated
@RequiredArgsConstructor
@Slf4j
public class WarehouseController implements WarehouseApi {
    private final WarehouseService warehouseService;

    // Добавить новый товар на склад.
    @Override
    @Logging
    public void newProduct(NewProductInWarehouseRequest newRequest) {
        warehouseService.newProduct(newRequest);
    }

    // Предварительно проверить что количество товаров на складе достаточно для данной корзины товаров.
    @Override
    @Logging
    public BookedProductsDto checkProducts(ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProducts(shoppingCartDto);
    }

    // Принять товар на склад.
    @Override
    @Logging
    public void addProduct(AddProductToWarehouseRequest addRequest) {
        warehouseService.addProduct(addRequest);
    }

    // Передать товары в доставку
    @Override
    @Logging
    public void shipped(ShippedToDeliveryRequest shippedToDeliveryRequest) {
        warehouseService.shipped(shippedToDeliveryRequest);
    }

    // Принять возврат товаров на склад.
    @Override
    @Logging
    public void returnProducts(Map<UUID, Long> products) {
        warehouseService.returnProducts(products);
    }

    // Собрать товары к заказу для подготовки к отправке.
    @Override
    @Logging
    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest) {
        return warehouseService.assemblyProducts(assemblyProductsForOrderRequest);
    }

    // Предоставить адрес склада для расчёта доставки.
    @Override
    @Logging
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
