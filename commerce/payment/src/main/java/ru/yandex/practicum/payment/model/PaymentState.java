package ru.yandex.practicum.payment.model;

public enum PaymentState {
    PENDING, // ожидает оплаты;
    SUCCESS, // успешно оплачен;
    FAILED   // ошибка в процессе оплаты.
}
