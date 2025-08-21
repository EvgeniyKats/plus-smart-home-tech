package ru.yandex.practicum.interaction.api.order;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderApi {

    // Получить заказы пользователя.
    @GetMapping
    List<OrderDto> getUserOrders(@RequestParam String username, Pageable pageable);

    // Создать новый заказ в системе.
    @PutMapping
    OrderDto createOrder(@Valid @RequestBody CreateNewOrderRequest createNewOrderRequest);

    // Отменить заказ
    @PostMapping
    OrderDto cancelOrder(@RequestBody UUID orderId);

    // Возврат заказа.
    @PostMapping("/return")
    OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest productReturnRequest);

    // Оплата заказа.
    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId);

    // Оплата заказа успешна.
    @PostMapping("/payment/success")
    OrderDto setStatusPaymentSuccess(UUID orderId);

    // Оплата заказа произошла с ошибкой.
    @PostMapping("/payment/failed")
    OrderDto setStatusPaymentFailed(@RequestBody UUID orderId);

    // Заказ находится в пункте получения
    @PostMapping("/delivery/pickup")
    OrderDto setStatusOnPickup(@RequestBody UUID orderId);

    // Заказ был получен покупателем из доставки
    @PostMapping("/done")
    OrderDto setStatusDone(@RequestBody UUID orderId);

    // Доставка заказа произошла с ошибкой.
    @PostMapping("/delivery/failed")
    OrderDto setStatusDeliveryFailed(@RequestBody UUID orderId);

    // Заказ был принят службой доставки
    @PostMapping("/delivery/start")
    OrderDto setStatusOnDelivery(@RequestBody UUID orderId);

    // Завершение заказа.
    @PostMapping("/completed")
    OrderDto setStatusCompleted(@RequestBody UUID orderId);

    // Расчёт итоговой стоимости заказа.
    @PostMapping("/calculate/total")
    OrderDto getTotalCost(@RequestBody UUID orderId);

    // Расчёт стоимости доставки заказа.
    @PostMapping("/calculate/delivery")
    OrderDto getDeliveryCost(@RequestBody UUID orderId);

    // Сборка заказа.
    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId);

    // Сборка заказа произошла с ошибкой.
    @PostMapping("/assembly/failed")
    OrderDto setStatusAssemblyFailed(@RequestBody UUID orderId);
}
