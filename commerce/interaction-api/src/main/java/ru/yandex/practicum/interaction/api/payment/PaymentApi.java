package ru.yandex.practicum.interaction.api.payment;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentApi {

    // Формирование оплаты для заказа (переход в платежный шлюз).
    @PostMapping
    PaymentDto createPayment(@Valid @RequestBody OrderDto orderDto);

    // Расчёт полной стоимости заказа.
    @PostMapping("/totalCost")
    BigDecimal getTotalCost(@Valid @RequestBody OrderDto orderDto);

    // Метод для эмуляции успешной оплаты в платежном шлюзе.
    @PostMapping("/refund")
    void setPaymentSuccess(@RequestBody UUID paymentId);

    // Расчёт стоимости товаров в заказе.
    @PostMapping("/productCost")
    BigDecimal getProductCost(@RequestBody OrderDto orderDto);

    // Метод для эмуляции отказа в оплате платежного шлюза.
    @PostMapping("/failed")
    void setPaymentFailed(@RequestBody UUID paymentId);
}
