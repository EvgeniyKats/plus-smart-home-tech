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
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.warehouse.service.WarehouseService;

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

    // Предоставить адрес склада для расчёта доставки.
    @Override
    @Logging
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
