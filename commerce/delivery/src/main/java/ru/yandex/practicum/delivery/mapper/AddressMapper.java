package ru.yandex.practicum.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    Address toAddress(AddressDto address);
}