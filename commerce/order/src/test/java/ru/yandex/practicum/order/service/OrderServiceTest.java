package ru.yandex.practicum.order.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.feign.delivery.DeliveryClientFeign;
import ru.yandex.practicum.interaction.client.feign.payment.PaymentClientFeign;
import ru.yandex.practicum.interaction.client.feign.shopping.cart.ShoppingCartClientFeign;
import ru.yandex.practicum.interaction.client.feign.warehouse.WarehouseClientFeign;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.model.OrderDeliveryDetails;
import ru.yandex.practicum.order.model.OrderProductsDetails;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @MockBean
    WarehouseClientFeign warehouseClientFeign;

    @MockBean
    DeliveryClientFeign deliveryClientFeign;

    @MockBean
    ShoppingCartClientFeign shoppingCartClientFeign;

    @MockBean
    PaymentClientFeign paymentClientFeign;

    @Test
    void createOrder() {
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder()
                .deliveryVolume(new BigDecimal("1"))
                .deliveryWeight(new BigDecimal("2"))
                .fragile(true)
                .build();

        when(warehouseClientFeign.checkProducts(any()))
                .thenReturn(bookedProductsDto);

        AddressDto fromAddress = AddressDto.builder()
                .country("from")
                .city("from")
                .street("from")
                .house("from")
                .flat("from")
                .build();

        when(warehouseClientFeign.getAddress())
                .thenReturn(fromAddress);

        AddressDto toAddress = AddressDto.builder()
                .country("country")
                .city("city")
                .street("street")
                .house("house")
                .flat("flat")
                .build();

        when(deliveryClientFeign.createDelivery(any()))
                .thenReturn(DeliveryDto.builder()
                        .orderId(UUID.randomUUID())
                        .deliveryId(UUID.randomUUID())
                        .fromAddress(fromAddress)
                        .toAddress(toAddress)
                        .deliveryState(DeliveryState.CREATED)
                        .build());

        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .shoppingCartId(UUID.randomUUID())
                .products(Map.of(UUID.randomUUID(), 5L, UUID.randomUUID(), 7L))
                .build();

        CreateNewOrderRequest request = CreateNewOrderRequest.builder()
                .shoppingCart(cartDto)
                .deliveryAddress(toAddress)
                .build();

        OrderDto orderDto = orderService.createOrder(request);

        Order orderInDb = orderRepository.findById(orderDto.getOrderId()).orElse(null);
        assertNotNull(orderInDb);

        Mockito.verify(warehouseClientFeign, Mockito.times(1))
                .checkProducts(any());

        Mockito.verify(warehouseClientFeign, Mockito.times(1))
                .getAddress();

        Mockito.verify(deliveryClientFeign, Mockito.times(1))
                .createDelivery(any());
    }

    @Test
    void getUserOrders() {
        UUID cartId = UUID.randomUUID();

        Order order1 = createTestOrder();
        order1.setShoppingCartId(cartId);

        Order order2 = createTestOrder();
        order2.setShoppingCartId(cartId);

        when(shoppingCartClientFeign.getShoppingCart(anyString()))
                .thenReturn(ShoppingCartDto.builder()
                        .shoppingCartId(cartId)
                        .build());
        List<OrderDto> ans = orderService.getUserOrders("Ivan", Pageable.unpaged());

        assertEquals(2, ans.size());
    }

    @Test
    void cancelOrder() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_DELIVERY);
        order.setPaymentId(UUID.randomUUID());
        orderService.cancelOrder(order.getOrderId());

        Mockito.verify(deliveryClientFeign, times(1))
                .setStatusCanceled(order.getDeliveryDetails().getDeliveryId());
        Mockito.verify(warehouseClientFeign, times(1))
                .returnProducts(order.getProductsDetails().getProducts());
        Mockito.verify(paymentClientFeign, times(1))
                .returnPayment(order.getPaymentId());
        Mockito.verify(paymentClientFeign, times(0))
                .setStatusCancel(order.getPaymentId());

        assertEquals(OrderState.CANCELED, order.getState());
    }

    @Test
    void returnOrder() {
        Order order = createTestOrder();
        order.setState(OrderState.DONE);

        ProductReturnRequest request = ProductReturnRequest.builder()
                .orderId(order.getOrderId())
                .products(order.getProductsDetails().getProducts())
                .build();

        orderService.returnOrder(request);
        Mockito.verify(warehouseClientFeign, times(1))
                .returnProducts(any());
        assertEquals(OrderState.PRODUCT_RETURNED, order.getState());
    }

    @Test
    void payment() {
        UUID paymentId = UUID.randomUUID();
        when(paymentClientFeign.createPayment(any()))
                .thenReturn(PaymentDto.builder()
                        .paymentId(paymentId)
                        .build());

        Order order = createTestOrder();
        order.setState(OrderState.NEW);
        orderService.payment(order.getOrderId());
        assertEquals(OrderState.ON_PAYMENT, order.getState());
        assertEquals(paymentId, order.getPaymentId());
    }

    @Test
    void setStatusPaymentSuccess() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_PAYMENT);
        orderService.setStatusPaymentSuccess(order.getOrderId());
        assertEquals(OrderState.PAID, order.getState());
    }

    @Test
    void setStatusPaymentFailed() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_PAYMENT);
        orderService.setStatusPaymentFailed(order.getOrderId());
        assertEquals(OrderState.PAYMENT_FAILED, order.getState());
    }

    @Test
    void setStatusOnPickup() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_DELIVERY);
        orderService.setStatusOnPickup(order.getOrderId());
        assertEquals(OrderState.ON_PICKUP, order.getState());
    }

    @Test
    void setStatusDone() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_PICKUP);
        orderService.setStatusDone(order.getOrderId());
        assertEquals(OrderState.DONE, order.getState());
    }

    @Test
    void setStatusDeliveryFailed() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_DELIVERY);
        orderService.setStatusDeliveryFailed(order.getOrderId());
        assertEquals(OrderState.DELIVERY_FAILED, order.getState());
    }

    @Test
    @Transactional
    void setStatusOnDelivery() {
        Order order = createTestOrder();
        order.setState(OrderState.ASSEMBLED);
        orderService.setStatusOnDelivery(order.getOrderId());
        assertEquals(OrderState.ON_DELIVERY, order.getState());
    }

    @Test
    void setStatusCompleted() {
        Order order = createTestOrder();
        order.setState(OrderState.ON_PICKUP);
        orderService.setStatusCompleted(order.getOrderId());
        assertEquals(OrderState.COMPLETED, order.getState());
    }

    @Test
    void getTotalCost() {
        Order order = createTestOrder();
        BigDecimal totalCost = new BigDecimal("10.0");
        when(paymentClientFeign.calculateTotalCost(any()))
                .thenReturn(totalCost);
        orderService.getTotalCost(order.getOrderId());
        assertEquals(totalCost, order.getTotalPrice());
    }

    @Test
    void getDeliveryCost() {
        Order order = createTestOrder();
        BigDecimal deliveryCost = new BigDecimal("6.0");
        when(deliveryClientFeign.calculateDeliveryCost(any()))
                .thenReturn(deliveryCost);
        orderService.getDeliveryCost(order.getOrderId());
        assertEquals(deliveryCost, order.getDeliveryDetails().getDeliveryPrice());
    }

    @Test
    void assembly() {
        Order order = createTestOrder();
        order.setState(OrderState.PAID);
        orderService.assembly(order.getOrderId());

        Mockito.verify(warehouseClientFeign, times(1))
                .assemblyProducts(any());

        assertEquals(OrderState.ASSEMBLED, order.getState());
    }

    @Test
    void setStatusAssemblyFailed() {
        Order order = createTestOrder();
        order.setState(OrderState.PAID);
        orderService.setStatusAssemblyFailed(order.getOrderId());
        assertEquals(OrderState.ASSEMBLY_FAILED, order.getState());
    }

    private Order createTestOrder() {
        Order order = Order.builder()
                .productsDetails(OrderProductsDetails.builder()
                        .products(Map.of(UUID.randomUUID(), 1L))
                        .build())
                .deliveryDetails(OrderDeliveryDetails.builder()
                        .deliveryId(UUID.randomUUID())
                        .build())
                .build();
        orderRepository.save(order);
        return order;
    }
}