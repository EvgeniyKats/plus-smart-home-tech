package ru.yandex.practicum.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.order.model.Order;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(source = "deliveryDetails.deliveryId", target = "deliveryId")
    @Mapping(source = "deliveryDetails.deliveryWeight", target = "deliveryWeight")
    @Mapping(source = "deliveryDetails.deliveryVolume", target = "deliveryVolume")
    @Mapping(source = "deliveryDetails.fragile", target = "fragile")
    @Mapping(source = "deliveryDetails.deliveryPrice", target = "deliveryPrice")
    @Mapping(source = "productsDetails.productPrice", target = "productPrice")
    @Mapping(source = "productsDetails.products", target = "products")
    OrderDto toOrderDto(Order order);
}
