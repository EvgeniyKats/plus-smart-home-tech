package ru.yandex.practicum.order.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public List<OrderDto> getUserOrders(String username) {
        return List.of();
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        return null;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return null;
    }

    @Override
    public OrderDto payment(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto setPaymentFailed(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto setDeliveryFailed(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto complete(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto setAssemblyFailed(UUID orderId) {
        return null;
    }
}
