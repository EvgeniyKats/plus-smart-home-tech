package ru.yandex.practicum.order.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    // Получить заказы пользователя.
    List<OrderDto> getUserOrders(String username, Pageable pageable);

    // Создать новый заказ в системе.
    OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest);

    // Возврат заказа.
    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    // Оплата заказа.
    OrderDto setPaymentSuccess(UUID orderId);

    // Оплата заказа произошла с ошибкой.
    OrderDto setPaymentFailed(UUID orderId);

    // Доставка заказа.
    OrderDto setDeliverySuccess(UUID orderId);

    // Доставка заказа произошла с ошибкой.
    OrderDto setDeliveryFailed(UUID orderId);

    // Завершение заказа.
    OrderDto setOrderCompleted(UUID orderId);

    // Расчёт итоговой стоимости заказа.
    OrderDto calculateTotalCost(UUID orderId);

    // Расчёт стоимости доставки заказа.
    OrderDto calculateDeliveryCost(UUID orderId);

    // Сборка заказа.
    OrderDto setAssemblySuccess(UUID orderId);

    // Сборка заказа произошла с ошибкой.
    OrderDto setAssemblyFailed(UUID orderId);
}
