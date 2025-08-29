package ru.yandex.practicum.interaction.client.feign.order;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.order.OrderApi;

@FeignClient(name = "order", path = "/api/v1/order", fallback = OrderFallback.class)
public interface OrderClientFeign extends OrderApi {
}
