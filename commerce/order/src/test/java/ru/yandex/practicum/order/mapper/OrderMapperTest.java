package ru.yandex.practicum.order.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.model.OrderDeliveryDetails;
import ru.yandex.practicum.order.model.OrderProductsDetails;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void shouldMapAllFieldsByOrder() {

        OrderProductsDetails orderProductsDetails = OrderProductsDetails.builder()
                .products(Map.of(UUID.randomUUID(), 5L, UUID.randomUUID(), 1L))
                .productPrice(new BigDecimal("8888"))
                .build();

        OrderDeliveryDetails orderDeliveryDetails = OrderDeliveryDetails.builder()
                .deliveryId(UUID.randomUUID())
                .deliveryPrice(new BigDecimal("77.0"))
                .deliveryWeight(new BigDecimal("5"))
                .deliveryVolume(new BigDecimal("1"))
                .fragile(true)
                .build();

        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .paymentId(UUID.randomUUID())
                .shoppingCartId(UUID.randomUUID())
                .totalPrice(new BigDecimal("123.123"))
                .state(OrderState.DELIVERY_FAILED)
                .productsDetails(orderProductsDetails)
                .deliveryDetails(orderDeliveryDetails)
                .build();

        OrderDto orderDto = orderMapper.toOrderDto(order);

        assertEquals(order.getOrderId(), orderDto.getOrderId());
        assertEquals(order.getPaymentId(), orderDto.getPaymentId());
        assertEquals(order.getShoppingCartId(), orderDto.getShoppingCartId());
        assertEquals(order.getTotalPrice(), orderDto.getTotalPrice());
        assertEquals(order.getState(), orderDto.getState());

        assertEquals(orderDeliveryDetails.getDeliveryId(), orderDto.getDeliveryId());
        assertEquals(orderDeliveryDetails.getDeliveryVolume(), orderDto.getDeliveryVolume());
        assertEquals(orderDeliveryDetails.getDeliveryWeight(), orderDto.getDeliveryWeight());
        assertEquals(orderDeliveryDetails.getDeliveryPrice(), orderDto.getDeliveryPrice());
        assertEquals(orderDeliveryDetails.getFragile(), orderDto.getFragile());

        assertEquals(orderProductsDetails.getProducts(), orderDto.getProducts());
        assertEquals(orderProductsDetails.getProductPrice(), orderDto.getProductPrice());
    }
}