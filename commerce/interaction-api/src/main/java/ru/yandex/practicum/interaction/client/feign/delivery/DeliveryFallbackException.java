package ru.yandex.practicum.interaction.client.feign.delivery;

public class DeliveryFallbackException extends RuntimeException {
    public DeliveryFallbackException() {
        super("Сервис delivery временно недоступен");
    }
}
