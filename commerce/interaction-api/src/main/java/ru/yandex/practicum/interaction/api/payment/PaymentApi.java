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
    BigDecimal calculateTotalCost(@Valid @RequestBody OrderDto orderDto);

    // Метод для эмуляции успешной оплаты в платежном шлюзе.
    @PostMapping("/refund")
    void success(@RequestBody UUID paymentId);

    // Расчёт стоимости товаров в заказе.
    @PostMapping("/productCost")
    BigDecimal calculateProductCost(@RequestBody OrderDto orderDto);

    // Метод для эмуляции отказа в оплате платежного шлюза.
    @PostMapping("/failed")
    void failed(@RequestBody UUID paymentId);

    // Метод для эмуляции отмены заявки на оплату.
    @PostMapping("/cancel")
    void cancel(@RequestBody UUID paymentId);

    // Метод для эмуляции возврата оплаты.
    @PostMapping("/return")
    void returnPayment(@RequestBody UUID paymentId);
}
