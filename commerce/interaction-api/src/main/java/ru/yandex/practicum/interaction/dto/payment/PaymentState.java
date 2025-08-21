package ru.yandex.practicum.interaction.dto.payment;

public enum PaymentState {
    PENDING,  // ожидает оплаты;
    SUCCESS,  // успешно оплачен;
    FAILED,   // ошибка в процессе оплаты;
    CANCELED, // оплата отменена;
    RETURNED  // средства возвращены покупателю
}
