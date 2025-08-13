package ru.yandex.practicum.interaction.dto.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {

    // Идентификатор доставки.
    @NotNull
    UUID deliveryId;

    // Адрес отправителя (склад).
    @Valid
    @NotNull
    AddressDto fromAddress;

    // Адрес получателя (пользователя).
    @Valid
    @NotNull
    AddressDto toAddress;

    // Идентификатор заказа.
    @NotNull
    UUID orderId;

    // Статус доставки.
    @NotNull
    DeliveryState deliveryState;
}
