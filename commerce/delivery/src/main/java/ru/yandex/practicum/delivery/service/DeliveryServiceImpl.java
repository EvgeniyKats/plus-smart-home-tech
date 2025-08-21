package ru.yandex.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.mapper.AddressMapper;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.delivery.util.calculate.DeliveryCalculate;
import ru.yandex.practicum.delivery.util.calculate.param.CalculateDeliveryCostParam;
import ru.yandex.practicum.interaction.client.feign.order.OrderClientFeign;
import ru.yandex.practicum.interaction.client.feign.warehouse.WarehouseClientFeign;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.exception.delivery.DeliveryChangeStateException;
import ru.yandex.practicum.interaction.exception.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.logging.Logging;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryCalculate deliveryCalculate;

    private final DeliveryMapper deliveryMapper;
    private final AddressMapper addressMapper;

    private final OrderClientFeign orderClientFeign;
    private final WarehouseClientFeign warehouseClientFeign;

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = Delivery.builder()
                .fromAddress(addressMapper.toAddress(deliveryDto.getFromAddress()))
                .toAddress(addressMapper.toAddress(deliveryDto.getToAddress()))
                .orderId(deliveryDto.getOrderId())
                .build();

        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    @Logging(Level.TRACE)
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        Delivery delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                .orElseThrow(NoDeliveryFoundException::new);

        CalculateDeliveryCostParam calculateDeliveryCostParam = CalculateDeliveryCostParam.builder()
                .fromAddress(delivery.getFromAddress())
                .toAddress(delivery.getToAddress())
                .weight(orderDto.getDeliveryWeight())
                .volume(orderDto.getDeliveryVolume())
                .fragile(orderDto.getFragile())
                .build();

        return deliveryCalculate.calculateDeliveryCost(calculateDeliveryCostParam);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void picked(UUID orderId) {
        Delivery delivery = deliveryRepository.findById(orderId)
                .orElseThrow(NoDeliveryFoundException::new);

        changeDeliveryStateWithCheck(delivery, Set.of(DeliveryState.CREATED), DeliveryState.IN_PROGRESS);

        ShippedToDeliveryRequest shippedToDeliveryRequest = ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId())
                .build();

        warehouseClientFeign.shipped(shippedToDeliveryRequest);
        log.trace("Отправлена информация о начале доставки в сервис warehouse");
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void success(UUID orderId) {
        Delivery delivery = deliveryRepository.findById(orderId)
                .orElseThrow(NoDeliveryFoundException::new);

        changeDeliveryStateWithCheck(delivery, Set.of(DeliveryState.IN_PROGRESS), DeliveryState.SUCCESS);

        orderClientFeign.setStatusDone(orderId);
        log.trace("Отправлена информация о успешной доставки в сервис order");
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void failed(UUID orderId) {
        Delivery delivery = deliveryRepository.findById(orderId)
                .orElseThrow(NoDeliveryFoundException::new);

        changeDeliveryStateWithCheck(delivery,
                Set.of(DeliveryState.CREATED, DeliveryState.IN_PROGRESS),
                DeliveryState.FAILED);

        orderClientFeign.setStatusDeliveryFailed(orderId);
        log.trace("Отправлена информация о неудачной доставке в сервис order");
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void setStatusCanceled(UUID orderId) {
        Delivery delivery = deliveryRepository.findById(orderId)
                .orElseThrow(NoDeliveryFoundException::new);

        changeDeliveryStateWithCheck(delivery,
                Set.of(DeliveryState.CREATED, DeliveryState.IN_PROGRESS),
                DeliveryState.CANCELLED);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void onPickup(UUID orderId) {
        Delivery delivery = deliveryRepository.findById(orderId)
                .orElseThrow(NoDeliveryFoundException::new);

        changeDeliveryStateWithCheck(delivery,
                Set.of(DeliveryState.IN_PROGRESS),
                DeliveryState.ON_PICKUP);

        orderClientFeign.setStatusOnPickup(orderId);
        log.trace("Отправлена информация о доставке до ПВЗ в сервис order");
    }

    /**
     * Меняет статус доставки, если текущий статус соответствует ожидаемому
     *
     * @throws DeliveryChangeStateException если текущий статус отличается от ожидаемого
     */
    @Logging(Level.DEBUG)
    private void changeDeliveryStateWithCheck(Delivery delivery,
                                              Set<DeliveryState> expectedStates,
                                              DeliveryState newState) {
        if (!expectedStates.contains(delivery.getDeliveryState())) {
            throw new DeliveryChangeStateException(expectedStates, delivery.getDeliveryState(), newState);
        }

        delivery.setDeliveryState(newState);
    }
}
