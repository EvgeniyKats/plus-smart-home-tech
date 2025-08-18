package ru.yandex.practicum.interaction.exception.delivery;

import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction.exception.BaseServiceException;

import java.util.Set;

/**
 * Выбрасывается, если производится попытка изменить статус доставки при несоответствующем статусе
 */
public class DeliveryChangeStateException extends BaseServiceException {
    /**
     * @param expected ожидаемый статус для успешного изменения текущего статуса на новый
     * @param current  текущий статус
     * @param newState новый статус
     */
    public DeliveryChangeStateException(Set<DeliveryState> expected, DeliveryState current, DeliveryState newState) {
        this.httpStatus = "400";
        this.userMessage = String.format("Текущий статус доставки: %s отличается от ожидаемого: %s, новый статус: %s",
                current,
                expected,
                newState);
    }
}
