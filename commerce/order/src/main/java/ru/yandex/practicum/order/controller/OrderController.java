package ru.yandex.practicum.order.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.order.OrderApi;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.logging.Logging;
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
    @Logging
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        return orderService.getUserOrders(username, pageable);
    }

    @Override
    @Logging
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        return orderService.createNewOrder(createNewOrderRequest);
    }

    @Override
    @Logging
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return orderService.returnOrder(productReturnRequest);
    }

    @Override
    @Logging
    public OrderDto setPaymentSuccess(UUID orderId) {
        return orderService.setPaymentSuccess(orderId);
    }

    @Override
    @Logging
    public OrderDto setPaymentFailed(UUID orderId) {
        return orderService.setPaymentFailed(orderId);
    }

    @Override
    @Logging
    public OrderDto setDeliverySuccess(UUID orderId) {
        return orderService.setDeliverySuccess(orderId);
    }

    @Override
    @Logging
    public OrderDto setDeliveryFailed(UUID orderId) {
        return orderService.setDeliveryFailed(orderId);
    }

    @Override
    @Logging
    public OrderDto setOrderCompleted(UUID orderId) {
        return orderService.setOrderCompleted(orderId);
    }

    @Override
    @Logging
    public OrderDto calculateTotalCost(UUID orderId) {
        return orderService.calculateTotalCost(orderId);
    }

    @Override
    @Logging
    public OrderDto calculateDeliveryCost(UUID orderId) {
        return orderService.calculateDeliveryCost(orderId);
    }

    @Override
    @Logging
    public OrderDto setAssemblySuccess(UUID orderId) {
        return orderService.setAssemblySuccess(orderId);
    }

    @Override
    @Logging
    public OrderDto setAssemblyFailed(UUID orderId) {
        return orderService.setAssemblyFailed(orderId);
    }
}
