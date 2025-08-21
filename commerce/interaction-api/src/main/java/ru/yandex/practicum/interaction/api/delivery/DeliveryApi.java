package ru.yandex.practicum.interaction.api.delivery;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryApi {

    // Создать новую доставку в БД.
    @PutMapping
    DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto deliveryDto);

    // Расчёт полной стоимости доставки заказа.
    @PostMapping("/cost")
    BigDecimal calculateDeliveryCost(@Valid @RequestBody OrderDto orderDto);

    // Эмуляция получения товара в доставку.
    @PostMapping("/picked")
    void picked(@RequestBody UUID orderId);

    // Эмуляция успешной доставки товара.
    @PostMapping("/successful")
    void success(@RequestBody UUID orderId);

    // Эмуляция успешной доставки товара до пункта выдачи.
    @PostMapping("/pickup")
    void onPickup(@RequestBody UUID orderId);

    // Эмуляция отмены доставки товаров.
    @PostMapping("/cancel")
    void setStatusCanceled(@RequestBody UUID orderId);

    // Эмуляция неудачной доставки, например товар не вручен или не может быть доставлен.
    @PostMapping("/failed")
    void failed(@RequestBody UUID orderId);

}
