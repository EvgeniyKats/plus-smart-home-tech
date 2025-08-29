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
    public OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest) {
        return orderService.createOrder(createNewOrderRequest);
    }

    @Override
    @Logging
    public OrderDto cancelOrder(UUID orderId) {
        return orderService.cancelOrder(orderId);
    }

    @Override
    @Logging
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return orderService.returnOrder(productReturnRequest);
    }

    @Override
    @Logging
    public OrderDto payment(UUID orderId) {
        return orderService.payment(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusPaymentSuccess(UUID orderId) {
        return orderService.setStatusPaymentSuccess(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusPaymentFailed(UUID orderId) {
        return orderService.setStatusPaymentFailed(orderId);
    }

    @Override
    public OrderDto setStatusOnPickup(UUID orderId) {
        return orderService.setStatusOnPickup(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusDone(UUID orderId) {
        return orderService.setStatusDone(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusDeliveryFailed(UUID orderId) {
        return orderService.setStatusDeliveryFailed(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusOnDelivery(UUID orderId) {
        return orderService.setStatusOnDelivery(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusCompleted(UUID orderId) {
        return orderService.setStatusCompleted(orderId);
    }

    @Override
    @Logging
    public OrderDto getTotalCost(UUID orderId) {
        return orderService.getTotalCost(orderId);
    }

    @Override
    @Logging
    public OrderDto getDeliveryCost(UUID orderId) {
        return orderService.getDeliveryCost(orderId);
    }

    @Override
    @Logging
    public OrderDto assembly(UUID orderId) {
        return orderService.assembly(orderId);
    }

    @Override
    @Logging
    public OrderDto setStatusAssemblyFailed(UUID orderId) {
        return orderService.setStatusAssemblyFailed(orderId);
    }
}
