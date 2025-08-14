package ru.yandex.practicum.order.service;

import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.logging.Logging;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    @Logging(Level.TRACE)
    public List<OrderDto> getUserOrders(String username) {
        return List.of();
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto payment(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setPaymentFailed(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto delivery(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setDeliveryFailed(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto complete(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto calculateTotalCost(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto calculateDeliveryCost(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto assembly(UUID orderId) {
        return null;
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setAssemblyFailed(UUID orderId) {
        return null;
    }
}
