package ru.yandex.practicum.interaction.dto.delivery;

public enum DeliveryState {
    CREATED,      // Создана
    IN_PROGRESS,  // В процессе
    DELIVERED,    // Доставка завершена успешно
    FAILED,       // Ошибка доставки
    CANCELLED     // Доставка отменена
}
