package ru.yandex.practicum.interaction.client.feign.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class OrderFallback implements OrderClientFeign {
    @Override
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto cancelOrder(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto payment(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusPaymentSuccess(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusPaymentFailed(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusOnPickup(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusDone(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusDeliveryFailed(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusOnDelivery(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusCompleted(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto getTotalCost(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto getDeliveryCost(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }

    @Override
    public OrderDto setStatusAssemblyFailed(UUID orderId) {
        OrderFallbackException cause = new OrderFallbackException();
        log.error(cause.getMessage(), cause);
        throw cause;
    }
}
