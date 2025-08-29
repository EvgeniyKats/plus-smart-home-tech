package ru.yandex.practicum.interaction.client.feign.delivery;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.delivery.DeliveryApi;

@FeignClient(name = "delivery", path = "/api/v1/delivery", fallback = DeliveryFallback.class)
public interface DeliveryClientFeign extends DeliveryApi {
}
