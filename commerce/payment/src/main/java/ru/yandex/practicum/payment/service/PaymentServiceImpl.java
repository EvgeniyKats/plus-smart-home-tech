package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.feign.order.OrderClientFeign;
import ru.yandex.practicum.interaction.client.feign.shopping.store.ShoppingStoreClientFeign;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentState;
import ru.yandex.practicum.interaction.exception.order.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.interaction.exception.payment.PaymentChangeStateException;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.payment.config.PaymentConfig;
import ru.yandex.practicum.payment.mapper.PaymentMapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;
import ru.yandex.practicum.payment.util.calculate.PaymentCalculate;
import ru.yandex.practicum.payment.util.calculate.param.CalculateProductsCostParam;
import ru.yandex.practicum.payment.util.calculate.param.CalculateTotalCostParam;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final ShoppingStoreClientFeign shoppingStoreClientFeign;
    private final OrderClientFeign orderClientFeign;

    private final PaymentConfig paymentConfig;
    private final PaymentCalculate paymentCalculate;

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public PaymentDto createPayment(OrderDto orderDto) {
        if (orderDto.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Стоимость товаров не определена");
        }

        if (orderDto.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Стоимость доставки не определена");
        }

        if (orderDto.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Итоговая стоимость не определена");
        }

        BigDecimal fee = paymentCalculate.calculateFee(orderDto.getProductPrice(), paymentConfig.getFeePercent());

        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(fee)
                .build();
        paymentRepository.save(payment);

        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    @Logging(Level.TRACE)
    public BigDecimal getTotalCost(OrderDto orderDto) {
        if (orderDto.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Стоимость товаров не определена");
        }

        if (orderDto.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Стоимость доставки не определена");
        }

        CalculateTotalCostParam calculateTotalCostParam = CalculateTotalCostParam
                .builder()
                .productsCost(orderDto.getProductPrice())
                .deliveryCost(orderDto.getDeliveryPrice())
                .feePercent(paymentConfig.getFeePercent())
                .build();

        return paymentCalculate.calculateTotalCost(calculateTotalCostParam);
    }

    @Override
    @Logging(Level.TRACE)
    public BigDecimal getProductCost(OrderDto orderDto) {
        if (orderDto.getProducts().keySet().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Нет товаров для расчёта цен");
        }

        Set<UUID> productsToGetPrice = orderDto.getProducts().keySet();
        Map<UUID, BigDecimal> productsPrice = shoppingStoreClientFeign.getProductsPrice(productsToGetPrice);
        log.trace("Получена мапа цен от shopping-store");

        CalculateProductsCostParam calculateProductsCostParam = CalculateProductsCostParam.builder()
                .productsQuantity(orderDto.getProducts())
                .productsPrice(productsPrice)
                .build();

        return paymentCalculate.calculateProductsCost(calculateProductsCostParam);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void setPaymentSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(NoOrderFoundException::new);

        changePaymentStateWithCheck(payment, PaymentState.SUCCESS);

        UUID orderId = payment.getOrderId();
        OrderDto orderDto = orderClientFeign.setPaymentSuccess(orderId);

        log.debug("Установлен PaymentState.SUCCESS, order={}", orderDto);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void setPaymentFailed(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(NoOrderFoundException::new);

        changePaymentStateWithCheck(payment, PaymentState.FAILED);

        UUID orderId = payment.getOrderId();
        OrderDto orderDto = orderClientFeign.setPaymentFailed(orderId);

        log.debug("Установлен PaymentState.FAILED, order={}", orderDto);
    }

    /**
     * Меняет статус платежа, если текущий PaymentState.PENDING
     *
     * @throws PaymentChangeStateException если текущий статус отличается от PaymentState.PENDING
     */
    private void changePaymentStateWithCheck(Payment payment, PaymentState newState) {
        if (!payment.getPaymentState().equals(PaymentState.PENDING)) {
            throw new PaymentChangeStateException(PaymentState.PENDING, payment.getPaymentState(), newState);
        }
        payment.setPaymentState(newState);
        log.debug("paymentId = {}, new state = {}", payment.getPaymentId(), newState);
    }
}
