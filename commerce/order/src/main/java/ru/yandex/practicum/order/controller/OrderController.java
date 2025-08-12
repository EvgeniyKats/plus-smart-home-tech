package ru.yandex.practicum.order.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.order.OrderApi;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@Validated
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public List<OrderDto> getUserOrders(String username) {
        return orderService.getUserOrders(username);
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        return orderService.createNewOrder(createNewOrderRequest);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return orderService.returnOrder(productReturnRequest);
    }

    @Override
    public OrderDto payment(UUID orderId) {
        return orderService.payment(orderId);
    }

    @Override
    public OrderDto setPaymentFailed(UUID orderId) {
        return orderService.setPaymentFailed(orderId);
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        return orderService.delivery(orderId);
    }

    @Override
    public OrderDto setDeliveryFailed(UUID orderId) {
        return orderService.setDeliveryFailed(orderId);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        return orderService.complete(orderId);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        return orderService.calculateTotalCost(orderId);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        return orderService.calculateDeliveryCost(orderId);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        return orderService.assembly(orderId);
    }

    @Override
    public OrderDto setAssemblyFailed(UUID orderId) {
        return orderService.setAssemblyFailed(orderId);
    }
}
