package ru.yandex.practicum.delivery.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressMapperTest {

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

    @Test
    void shouldMapAllFieldsFromAddressDto() {

        AddressDto addressDto = AddressDto.builder()
                .country("country")
                .city("city")
                .street("street")
                .house("house")
                .flat("flat")
                .build();

        Address address = addressMapper.toAddress(addressDto);

        assertEquals(addressDto.getCountry(), address.getCountry());
        assertEquals(addressDto.getCity(), address.getCity());
        assertEquals(addressDto.getStreet(), address.getStreet());
        assertEquals(addressDto.getHouse(), address.getHouse());
        assertEquals(addressDto.getFlat(), address.getFlat());
    }

    @Test
    void shouldMapAllFieldsFromAddress() {

        Address address = Address.builder()
                .id(UUID.randomUUID())
                .country("country")
                .city("city")
                .street("street")
                .house("house")
                .flat("flat")
                .build();

        AddressDto addressDto = addressMapper.toAddressDto(address);

        assertEquals(address.getCountry(), addressDto.getCountry());
        assertEquals(address.getCity(), addressDto.getCity());
        assertEquals(address.getStreet(), addressDto.getStreet());
        assertEquals(address.getHouse(), addressDto.getHouse());
        assertEquals(address.getFlat(), addressDto.getFlat());
    }
}