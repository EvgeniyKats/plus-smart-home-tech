package ru.yandex.practicum.interaction.client.feign.warehouse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClientFeign {
    // Добавить новый товар на склад.
    @PutMapping("/api/v1/warehouse")
    void newProduct(NewProductInWarehouseRequest newRequest);

    // Предварительно проверить что количество товаров на складе достаточно для данной корзины товаров.
    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkProducts(ShoppingCartDto shoppingCartDto);

    // Принять товар на склад.
    @PostMapping("/api/v1/warehouse/add")
    void addProduct(AddProductToWarehouseRequest addRequest);

    // Предоставить адрес склада для расчёта доставки.
    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress();
}
