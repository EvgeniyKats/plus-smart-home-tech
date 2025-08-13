package ru.yandex.practicum.interaction.client.feign.payment;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.payment.PaymentApi;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClientFeign extends PaymentApi {
}
