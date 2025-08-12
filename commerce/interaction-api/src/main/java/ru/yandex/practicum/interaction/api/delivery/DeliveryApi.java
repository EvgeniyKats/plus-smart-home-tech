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
    BigDecimal getCost(@Valid @RequestBody OrderDto orderDto);

    // Эмуляция получения товара в доставку.
    @PostMapping("/picked")
    void picked(@RequestBody UUID orderId);

    // Эмуляция успешной доставки товара.
    @PostMapping("/successful")
    void setSuccess(@RequestBody UUID orderId);

    // Эмуляция неудачного вручения товара.
    @PostMapping("/failed")
    void setFailed(@RequestBody UUID orderId);
}
