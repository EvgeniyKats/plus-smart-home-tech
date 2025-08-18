package ru.yandex.practicum.interaction.exception.payment;

import ru.yandex.practicum.interaction.dto.payment.PaymentState;
import ru.yandex.practicum.interaction.exception.BaseServiceException;

public class PaymentChangeStateException extends BaseServiceException {
    /**
     * Выбрасывается, если производится попытка изменить статус оплаты при несоответствующем статусе
     *
     * @param expected - ожидаемый статус для успешного изменения текущего статуса на новый
     * @param current  - текущий статус
     * @param newState - новый статус
     */
    public PaymentChangeStateException(PaymentState expected, PaymentState current, PaymentState newState) {
        this.httpStatus = "400";
        this.userMessage = String.format("Текущий статус оплаты: %s отличается от ожидаемого: %s, новый статус: %s",
                current,
                expected,
                newState);
    }
}
