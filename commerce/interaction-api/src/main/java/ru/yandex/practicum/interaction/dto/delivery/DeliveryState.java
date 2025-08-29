package ru.yandex.practicum.interaction.dto.delivery;

public enum DeliveryState {
    CREATED,      // Создана
    IN_PROGRESS,  // В процессе
    ON_PICKUP,    //
    SUCCESS,      // Доставка завершена успешно
    FAILED,       // Ошибка доставки (например, покупатель не пришёл за заказом или
    CANCELLED     // Доставка отменена (без ошибок)
}
