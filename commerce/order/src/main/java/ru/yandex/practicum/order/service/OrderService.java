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
    OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest);

    // Отменить заказ
    OrderDto cancelOrder(UUID orderId);

    // Возврат заказа.
    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    // Оплата заказа.
    OrderDto payment(UUID orderId);

    // Оплата заказа успешна.
    OrderDto setStatusPaymentSuccess(UUID orderId);

    // Оплата заказа произошла с ошибкой.
    OrderDto setStatusPaymentFailed(UUID orderId);

    // Заказ находится в пункте получения
    OrderDto setStatusOnPickup(UUID orderId);

    // Доставка заказа произошла успешно до ПВЗ.
    OrderDto setStatusDone(UUID orderId);

    // Доставка заказа произошла с ошибкой.
    OrderDto setStatusDeliveryFailed(UUID orderId);

    // Заказ был принят службой доставки
    OrderDto setStatusOnDelivery(UUID orderId);

    // Завершение заказа.
    OrderDto setStatusCompleted(UUID orderId);

    // Расчёт итоговой стоимости заказа.
    OrderDto getTotalCost(UUID orderId);

    // Расчёт стоимости доставки заказа.
    OrderDto getDeliveryCost(UUID orderId);

    // Сборка заказа.
    OrderDto assembly(UUID orderId);

    // Сборка заказа произошла с ошибкой.
    OrderDto setStatusAssemblyFailed(UUID orderId);
}
