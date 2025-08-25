package ru.yandex.practicum.delivery.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryMapperTest {

    private final DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);

    @Test
    void shouldMapAllFieldsFromDelivery() {
        Address from = Address.builder()
                .id(UUID.randomUUID())
                .country("from")
                .city("from")
                .street("from")
                .house("from")
                .flat("from")
                .build();

        Address to = Address.builder()
                .id(UUID.randomUUID())
                .country("to")
                .city("to")
                .street("to")
                .house("to")
                .flat("to")
                .build();

        Delivery delivery = Delivery.builder()
                .deliveryId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .fromAddress(from)
                .toAddress(to)
                .deliveryState(DeliveryState.IN_PROGRESS)
                .build();

        DeliveryDto deliveryDto = deliveryMapper.toDeliveryDto(delivery);

        assertEquals(delivery.getDeliveryId(), deliveryDto.getDeliveryId());
        assertEquals(delivery.getOrderId(), deliveryDto.getOrderId());
        assertEquals(delivery.getDeliveryState(), deliveryDto.getDeliveryState());

        checkAddressDtoByAddress(delivery.getFromAddress(), deliveryDto.getFromAddress());
        checkAddressDtoByAddress(delivery.getToAddress(), deliveryDto.getToAddress());
    }

    @Test
    void shouldMapAllFieldsFromDeliveryDto() {
        AddressDto from = AddressDto.builder()
                .country("from")
                .city("from")
                .street("from")
                .house("from")
                .flat("from")
                .build();

        AddressDto to = AddressDto.builder()
                .country("to")
                .city("to")
                .street("to")
                .house("to")
                .flat("to")
                .build();

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .deliveryId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .fromAddress(from)
                .toAddress(to)
                .deliveryState(DeliveryState.IN_PROGRESS)
                .build();

        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);

        assertEquals(deliveryDto.getDeliveryId(), delivery.getDeliveryId());
        assertEquals(deliveryDto.getOrderId(), delivery.getOrderId());
        assertEquals(deliveryDto.getDeliveryState(), delivery.getDeliveryState());

        checkAddressByAddressDto(deliveryDto.getFromAddress(), delivery.getFromAddress());
        checkAddressByAddressDto(deliveryDto.getToAddress(), delivery.getToAddress());
    }

    private void checkAddressDtoByAddress(Address expected, AddressDto actual) {
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getStreet(), actual.getStreet());
        assertEquals(expected.getHouse(), actual.getHouse());
        assertEquals(expected.getFlat(), actual.getFlat());
    }

    private void checkAddressByAddressDto(AddressDto expected, Address actual) {
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getStreet(), actual.getStreet());
        assertEquals(expected.getHouse(), actual.getHouse());
        assertEquals(expected.getFlat(), actual.getFlat());
    }
}