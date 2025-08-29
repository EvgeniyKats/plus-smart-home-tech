package ru.yandex.practicum.interaction.client.feign.order;

public class OrderFallbackException extends RuntimeException {
    public OrderFallbackException() {
        super("Сервис order временно недоступен");
    }
}
