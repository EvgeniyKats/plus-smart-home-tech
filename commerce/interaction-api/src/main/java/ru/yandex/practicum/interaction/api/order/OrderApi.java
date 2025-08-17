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
    OrderDto createNewOrder(@Valid @RequestBody CreateNewOrderRequest createNewOrderRequest);

    // Возврат заказа.
    @PostMapping("/return")
    OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest productReturnRequest);

    // Оплата заказа.
    @PostMapping("/payment/success")
    OrderDto setPaymentSuccess(@RequestBody UUID orderId);

    // Оплата заказа произошла с ошибкой.
    @PostMapping("/payment/failed")
    OrderDto setPaymentFailed(@RequestBody UUID orderId);

    // Доставка заказа произошла успешно до ПВЗ.
    @PostMapping("/delivery/success")
    OrderDto setDeliverySuccess(@RequestBody UUID orderId);

    // Заказ был получен покупателем из доставки
    @PostMapping("/delivery/received")
    OrderDto setDeliveryDone(@RequestBody UUID orderId);

    // Доставка заказа произошла с ошибкой.
    @PostMapping("/delivery/failed")
    OrderDto setDeliveryFailed(@RequestBody UUID orderId);

    // Завершение заказа.
    @PostMapping("/completed")
    OrderDto setOrderCompleted(@RequestBody UUID orderId);

    // Расчёт итоговой стоимости заказа.
    @PostMapping("/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId);

    // Расчёт стоимости доставки заказа.
    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId);

    // Сборка заказа.
    @PostMapping("/assembly/success")
    OrderDto setAssemblySuccess(@RequestBody UUID orderId);

    // Сборка заказа произошла с ошибкой.
    @PostMapping("/assembly/failed")
    OrderDto setAssemblyFailed(@RequestBody UUID orderId);
}
