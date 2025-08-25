package ru.yandex.practicum.delivery.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.mapper.AddressMapper;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.delivery.util.calculate.DeliveryCalculate;
import ru.yandex.practicum.interaction.client.feign.order.OrderClientFeign;
import ru.yandex.practicum.interaction.client.feign.warehouse.WarehouseClientFeign;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringBootTest
@Transactional
class DeliveryServiceTest {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    AddressMapper addressMapper;

    @MockBean
    OrderClientFeign orderClientFeign;

    @MockBean
    WarehouseClientFeign warehouseClientFeign;

    @MockBean
    DeliveryCalculate deliveryCalculate;

    @Test
    void createDelivery() {
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(addressMapper.toAddressDto(createTestFromAddress()))
                .toAddress(addressMapper.toAddressDto(createTestToAddress()))
                .orderId(UUID.randomUUID())
                .build();

        DeliveryDto ans = deliveryService.createDelivery(deliveryDto);
        assertNotNull(ans);
        assertNotNull(ans.getDeliveryId());

        Delivery delivery = deliveryRepository.findById(ans.getDeliveryId()).orElse(null);
        assertNotNull(delivery);
    }

    @Test
    void calculateDeliveryCost() {
        Delivery delivery = createTestDelivery();
        OrderDto orderDto = OrderDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .build();
        deliveryService.calculateDeliveryCost(orderDto);

        Mockito.verify(deliveryCalculate, times(1))
                .calculateDeliveryCost(any());
    }

    @Test
    void picked() {
        Delivery delivery = createTestDelivery();
        deliveryService.picked(delivery.getDeliveryId());
        Mockito.verify(warehouseClientFeign, times(1))
                .shipped(any());
        assertEquals(DeliveryState.IN_PROGRESS, delivery.getDeliveryState());
    }

    @Test
    void success() {
        Delivery delivery = createTestDelivery();
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryService.success(delivery.getDeliveryId());
        Mockito.verify(orderClientFeign, times(1))
                .setStatusDone(delivery.getOrderId());
        assertEquals(DeliveryState.SUCCESS, delivery.getDeliveryState());
    }

    @Test
    void failed() {
        Delivery delivery = createTestDelivery();
        deliveryService.failed(delivery.getDeliveryId());
        Mockito.verify(orderClientFeign, times(1))
                .setStatusDeliveryFailed(delivery.getOrderId());
        assertEquals(DeliveryState.FAILED, delivery.getDeliveryState());
    }

    @Test
    void setStatusCanceled() {
        Delivery delivery = createTestDelivery();
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryService.setStatusCanceled(delivery.getDeliveryId());
        assertEquals(DeliveryState.CANCELLED, delivery.getDeliveryState());
    }

    @Test
    void onPickup() {
        Delivery delivery = createTestDelivery();
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryService.onPickup(delivery.getDeliveryId());
        Mockito.verify(orderClientFeign, times(1))
                .setStatusOnPickup(delivery.getOrderId());
        assertEquals(DeliveryState.ON_PICKUP, delivery.getDeliveryState());
    }

    private Delivery createTestDelivery() {
        Address fromAddress = createTestFromAddress();
        Address toAddress = createTestToAddress();

        Delivery delivery = Delivery.builder()
                .orderId(UUID.randomUUID())
                .deliveryState(DeliveryState.CREATED)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .build();

        deliveryRepository.save(delivery);

        return delivery;
    }

    private Address createTestFromAddress() {
        return Address.builder()
                .country("from")
                .city("from")
                .street("from")
                .house("from")
                .flat("from")
                .build();
    }

    private Address createTestToAddress() {
        return Address.builder()
                .country("to")
                .city("to")
                .street("to")
                .house("to")
                .flat("to")
                .build();
    }
}