package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.interaction.api.delivery.DeliveryApi;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@Validated
@RequiredArgsConstructor
public class DeliveryController implements DeliveryApi {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    public BigDecimal getCost(OrderDto orderDto) {
        return deliveryService.getCost(orderDto);
    }

    @Override
    public void picked(UUID orderId) {
        deliveryService.picked(orderId);
    }

    @Override
    public void setSuccess(UUID orderId) {
        deliveryService.setSuccess(orderId);
    }

    @Override
    public void setFailed(UUID orderId) {
        deliveryService.setFailed(orderId);
    }
}
