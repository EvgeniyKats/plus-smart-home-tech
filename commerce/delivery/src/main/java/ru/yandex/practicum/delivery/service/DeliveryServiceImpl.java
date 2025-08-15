package ru.yandex.practicum.delivery.service;

import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.logging.Logging;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Override
    @Logging(Level.TRACE)
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public BigDecimal getCost(OrderDto orderDto) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public void picked(UUID orderId) {

    }

    @Override
    @Logging(Level.TRACE)
    public void setSuccess(UUID orderId) {

    }

    @Override
    @Logging(Level.TRACE)
    public void setFailed(UUID orderId) {

    }
}
