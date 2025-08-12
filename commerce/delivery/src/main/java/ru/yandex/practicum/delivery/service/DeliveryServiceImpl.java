package ru.yandex.practicum.delivery.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return null;
    }

    @Override
    public BigDecimal getCost(OrderDto orderDto) {
        return null;
    }

    @Override
    public void picked(UUID orderId) {

    }

    @Override
    public void setSuccess(UUID orderId) {

    }

    @Override
    public void setFailed(UUID orderId) {

    }
}
