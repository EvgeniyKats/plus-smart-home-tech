package ru.yandex.practicum.interaction.client.feign.payment;

public class PaymentFallbackException extends RuntimeException {
    public PaymentFallbackException() {
        super("Сервис payment временно недоступен");
    }
}
