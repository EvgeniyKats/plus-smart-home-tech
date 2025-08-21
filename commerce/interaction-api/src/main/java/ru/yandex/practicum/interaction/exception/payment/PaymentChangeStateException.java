package ru.yandex.practicum.interaction.exception.payment;

import ru.yandex.practicum.interaction.dto.payment.PaymentState;
import ru.yandex.practicum.interaction.exception.BaseServiceException;

import java.util.Set;

/**
 * Выбрасывается, если производится попытка изменить статус оплаты при несоответствующем статусе
 */
public class PaymentChangeStateException extends BaseServiceException {
    /**
     * @param expected ожидаемые статусы для успешного изменения текущего статуса на новый
     * @param current  текущий статус
     * @param newState новый статус
     */
    public PaymentChangeStateException(Set<PaymentState> expected, PaymentState current, PaymentState newState) {
        this.httpStatus = "400";
        this.userMessage = String.format("Текущий статус оплаты: %s отличается от ожидаемого: %s, новый статус: %s",
                current,
                expected,
                newState);
    }
}
