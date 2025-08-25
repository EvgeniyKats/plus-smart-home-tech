package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.feign.delivery.DeliveryClientFeign;
import ru.yandex.practicum.interaction.client.feign.payment.PaymentClientFeign;
import ru.yandex.practicum.interaction.client.feign.shopping.cart.ShoppingCartClientFeign;
import ru.yandex.practicum.interaction.client.feign.warehouse.WarehouseClientFeign;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.exception.order.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.order.OrderChangeStateException;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.model.OrderDeliveryDetails;
import ru.yandex.practicum.order.model.OrderProductsDetails;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ShoppingCartClientFeign shoppingCartClientFeign;
    private final DeliveryClientFeign deliveryClientFeign;
    private final WarehouseClientFeign warehouseClientFeign;
    private final PaymentClientFeign paymentClientFeign;

    @Override
    @Logging(Level.TRACE)
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        ShoppingCartDto shoppingCartDto = shoppingCartClientFeign.getShoppingCart(username);

        UUID cartId = shoppingCartDto.getShoppingCartId();
        List<Order> orders = orderRepository.findAllByShoppingCartIdWithProducts(cartId, pageable);

        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest) {
        // 1. Проверяем наличие товаров на складе
        ShoppingCartDto shoppingCartDto = createNewOrderRequest.getShoppingCart();
        BookedProductsDto bookedProductsDto = warehouseClientFeign.checkProducts(shoppingCartDto);
        log.trace("Наличие на складе проверено");

        // 2. Создаём новый order
        OrderDeliveryDetails orderDeliveryDetails = OrderDeliveryDetails.builder()
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .fragile(bookedProductsDto.isFragile())
                .build();

        OrderProductsDetails orderProductsDetails = OrderProductsDetails.builder()
                .products(createNewOrderRequest.getShoppingCart().getProducts())
                .build();

        Order order = Order.builder()
                .deliveryDetails(orderDeliveryDetails)
                .productsDetails(orderProductsDetails)
                .shoppingCartId(shoppingCartDto.getShoppingCartId())
                .build();
        orderRepository.save(order);
        log.trace("Сохранён новый order, id={}", order.getOrderId());

        // 3. Создаём заявку на доставку
        AddressDto fromAddress = warehouseClientFeign.getAddress();
        AddressDto toAddress = createNewOrderRequest.getDeliveryAddress();
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .orderId(order.getOrderId())
                .build();

        deliveryDto = deliveryClientFeign.createDelivery(deliveryDto);
        orderDeliveryDetails.setDeliveryId(deliveryDto.getDeliveryId());
        log.trace("Создана заявка на доставку, deliveryId={}", deliveryDto.getDeliveryId());

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        OrderState oldState = order.getState();
        log.debug("orderId={}, oldState={}", orderId, oldState);

        // 1. Смена статуса, если текущий статус подходит для отмены
        Set<OrderState> successStates = Set.of(
                OrderState.NEW,
                OrderState.ON_PAYMENT,
                OrderState.PAID,
                OrderState.ASSEMBLED,
                OrderState.ON_DELIVERY,
                OrderState.ON_PICKUP);

        changeOrderStateWithCheck(order, successStates, OrderState.CANCELED);

        // 2. Необходимо отменить доставку
        deliveryClientFeign.setStatusCanceled(orderId);
        log.trace("Передан статус отмены в службу доставки");

        // 3. Если заказ был собран или в доставке, необходимо увеличить количество товаров на складе
        boolean isAssembled = false;
        if (oldState.equals(OrderState.ASSEMBLED)
            || oldState.equals(OrderState.ON_PICKUP)
            || oldState.equals(OrderState.ON_DELIVERY)) {
            isAssembled = true;
            warehouseClientFeign.returnProducts(order.getProductsDetails().getProducts());
            log.trace("Увеличено количество товаров на складе");
        }

        // 4. Если заказ оплачен, производим возврат средств
        if (isAssembled || oldState.equals(OrderState.PAID)) {
            paymentClientFeign.returnPayment(order.getPaymentId());
            log.trace("Произведён возврат средств за заказ");
        }

        // 5. Если заказ в процессе оплаты, необходимо отменить заявку на оплату
        if (oldState.equals(OrderState.ON_PAYMENT)) {
            paymentClientFeign.cancel(order.getPaymentId());
            log.trace("Отменена заявка на оплату в платёжном шлюзе");
        }

        // 6. Логирование остальных статусов
        if (oldState.equals(OrderState.NEW)) {
            log.trace("У заказа был статус OrderState.NEW, другие действия не требуются");
        }

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        UUID orderId = productReturnRequest.getOrderId();
        Map<UUID, Long> products = productReturnRequest.getProducts();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.DONE, OrderState.COMPLETED), OrderState.PRODUCT_RETURNED);

        warehouseClientFeign.returnProducts(products);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto payment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);
        changeOrderStateWithCheck(order, Set.of(OrderState.NEW), OrderState.ON_PAYMENT);


        PaymentDto paymentDto = paymentClientFeign.createPayment(orderMapper.toOrderDto(order));
        order.setPaymentId(paymentDto.getPaymentId());
        log.trace("Создана заявка на оплату, paymentId={}", order.getPaymentId());

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusPaymentSuccess(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.ON_PAYMENT), OrderState.PAID);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusPaymentFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.ON_PAYMENT), OrderState.PAYMENT_FAILED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusOnPickup(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.ON_DELIVERY), OrderState.ON_PICKUP);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusDone(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order,
                Set.of(OrderState.ON_DELIVERY, OrderState.ON_PICKUP),
                OrderState.DONE);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusOnDelivery(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.ASSEMBLED), OrderState.ON_DELIVERY);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusDeliveryFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.ON_DELIVERY), OrderState.DELIVERY_FAILED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusCompleted(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.ON_DELIVERY, OrderState.ON_PICKUP), OrderState.COMPLETED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto getTotalCost(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        BigDecimal totalCost = paymentClientFeign.calculateTotalCost(orderMapper.toOrderDto(order));
        log.debug("paymentClientFeign.getTotalCost(): orderId={}, totalCost={}", orderId, totalCost);
        order.setTotalPrice(totalCost);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto getDeliveryCost(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        BigDecimal deliveryCost = deliveryClientFeign.calculateDeliveryCost(orderMapper.toOrderDto(order));
        log.debug("deliveryClientFeign.getCost(): orderId={}, deliveryCost={}", orderId, deliveryCost);
        order.getDeliveryDetails().setDeliveryPrice(deliveryCost);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto assembly(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);
        changeOrderStateWithCheck(order, Set.of(OrderState.PAID), OrderState.ASSEMBLED);

        AssemblyProductsForOrderRequest assemblyProductsForOrderRequest = AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(order.getProductsDetails().getProducts())
                .build();

        warehouseClientFeign.assemblyProducts(assemblyProductsForOrderRequest);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public OrderDto setStatusAssemblyFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderFoundException::new);

        changeOrderStateWithCheck(order, Set.of(OrderState.PAID), OrderState.ASSEMBLY_FAILED);

        return orderMapper.toOrderDto(order);
    }

    /**
     * Меняет статус заказа, если текущий статус соответствует ожидаемому
     *
     * @throws OrderChangeStateException если текущий статус отличается от ожидаемого
     */
    @Logging(Level.DEBUG)
    private void changeOrderStateWithCheck(Order order,
                                           Set<OrderState> expectedStates,
                                           OrderState newState) {
        if (!expectedStates.contains(order.getState())) {
            throw new OrderChangeStateException(expectedStates, order.getState(), newState);
        }

        order.setState(newState);
    }
}
