package ru.yandex.practicum.interaction.client.feign.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
public class DeliveryFallback implements DeliveryClientFeign {
    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void picked(UUID orderId) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void success(UUID orderId) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void onPickup(UUID orderId) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void setStatusCanceled(UUID orderId) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public void failed(UUID orderId) {
        DeliveryFallbackException cause = new DeliveryFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }
}
