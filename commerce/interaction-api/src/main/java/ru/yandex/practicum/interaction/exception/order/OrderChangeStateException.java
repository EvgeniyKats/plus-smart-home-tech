package ru.yandex.practicum.interaction.exception.order;

import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.interaction.exception.BaseServiceException;

import java.util.Set;

/**
 * Выбрасывается, если производится попытка изменить статус оплаты при несоответствующем статусе
 */
public class OrderChangeStateException extends BaseServiceException {
    /**
     * @param expected ожидаемый статус для успешного изменения текущего статуса на новый
     * @param current  текущий статус
     * @param newState новый статус
     */
    public OrderChangeStateException(Set<OrderState> expected, OrderState current, OrderState newState) {
        this.httpStatus = "400";
        this.userMessage = String.format("Текущий статус заказа: %s отличается от ожидаемого: %s, новый статус: %s",
                current,
                expected,
                newState);
    }
}
