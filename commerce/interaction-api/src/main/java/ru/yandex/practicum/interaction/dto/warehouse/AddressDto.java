package ru.yandex.practicum.interaction.dto.warehouse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Представление адреса в системе.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {
    // Страна
    String country;

    // Город
    String city;

    // Улица
    String street;

    // Дом
    String house;

    // Квартира
    String flat;
}
