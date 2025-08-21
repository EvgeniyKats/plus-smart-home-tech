package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.interaction.api.delivery.DeliveryApi;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.logging.Logging;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@Validated
@RequiredArgsConstructor
public class DeliveryController implements DeliveryApi {
    private final DeliveryService deliveryService;

    @Override
    @Logging
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    @Logging
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        return deliveryService.calculateDeliveryCost(orderDto);
    }

    @Override
    @Logging
    public void picked(UUID orderId) {
        deliveryService.picked(orderId);
    }

    @Override
    @Logging
    public void success(UUID orderId) {
        deliveryService.success(orderId);
    }

    @Override
    @Logging
    public void onPickup(UUID orderId) {
        deliveryService.onPickup(orderId);
    }

    @Override
    public void setStatusCanceled(UUID orderId) {
        deliveryService.setStatusCanceled(orderId);
    }

    @Override
    @Logging
    public void failed(UUID orderId) {
        deliveryService.failed(orderId);
    }
}
