package ru.yandex.practicum.interaction.api.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseApi {
    // Добавить новый товар на склад.
    @PutMapping
    void newProduct(@Valid @RequestBody NewProductInWarehouseRequest newRequest);

    // Предварительно проверить что количество товаров на складе достаточно для данной корзины товаров.
    @PostMapping("/check")
    BookedProductsDto checkProducts(@Valid @RequestBody ShoppingCartDto shoppingCartDto);

    // Принять товар на склад.
    @PostMapping("/add")
    void addProduct(@Valid @RequestBody AddProductToWarehouseRequest addRequest);

    // Передать товары в доставку
    @PostMapping("/shipped")
    void shipped(@Valid @RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest);

    // Принять возврат товаров на склад.
    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, @Positive Long> products);

    // Собрать товары к заказу для подготовки к отправке.
    @PostMapping("/assembly")
    BookedProductsDto assemblyProducts(@Valid @RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);

    // Предоставить адрес склада для расчёта доставки.
    @GetMapping("/address")
    AddressDto getAddress();
}
