package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.feign.delivery.DeliveryClientFeign;
import ru.yandex.practicum.interaction.client.feign.payment.PaymentClientFeign;
import ru.yandex.practicum.interaction.client.feign.shopping.cart.ShoppingCartClientFeign;
import ru.yandex.practicum.interaction.client.feign.warehouse.WarehouseClientFeign;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.order.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.shopping.cart.NotAuthorizedUserException;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// TODO: проверить корректное использование транзакций
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ShoppingCartClientFeign shoppingCartClientFeign;
    private final DeliveryClientFeign deliveryClientFeign;
    private final WarehouseClientFeign warehouseClientFeign;
    private final PaymentClientFeign paymentClientFeign;

    @Override
    @Logging(Level.TRACE)
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        validateUsername(username);

        // TODO: сейчас ищется только текущая корзина, история корзин не сохраняется
        ShoppingCartDto shoppingCartDto = shoppingCartClientFeign.getShoppingCart(username);

        List<UUID> shoppingCartIds = List.of(shoppingCartDto.getShoppingCartId());
        List<Order> orders = orderRepository.findAllByShoppingCartIdWithProducts(shoppingCartIds, pageable);

        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        // TODO: реализовать
        return null;
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        UUID orderId = productReturnRequest.getOrderId();
        Map<UUID, Long> products = productReturnRequest.getProducts();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.PRODUCT_RETURNED);

        // TODO: вызвать увеличение товаров на складе

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setPaymentSuccess(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.PAID);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setPaymentFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.PAYMENT_FAILED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setDeliverySuccess(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.DELIVERED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setDeliveryDone(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.DONE);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setDeliveryFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.DELIVERY_FAILED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setOrderCompleted(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.DONE);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        BigDecimal totalCost = paymentClientFeign.getTotalCost(orderMapper.toOrderDto(order));
        log.debug("paymentClientFeign.getTotalCost(): orderId={}, totalCost={}", orderId, totalCost);
        order.setTotalPrice(totalCost);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        BigDecimal cost = deliveryClientFeign.getCost(orderMapper.toOrderDto(order));
        log.debug("deliveryClientFeign.getCost(): orderId={}, cost={}", orderId, cost);
        order.getDelivery().setDeliveryPrice(cost);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setAssemblySuccess(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.ASSEMBLED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Logging(Level.TRACE)
    public OrderDto setAssemblyFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        order.setState(OrderState.ASSEMBLY_FAILED);

        return orderMapper.toOrderDto(order);
    }

    @Logging
    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException();
        }
    }
}
