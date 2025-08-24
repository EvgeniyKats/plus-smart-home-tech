package ru.yandex.practicum.interaction.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.client.feign.decoder.InternalServerErrorException;
import ru.yandex.practicum.interaction.client.feign.decoder.NotFoundException;
import ru.yandex.practicum.interaction.client.feign.delivery.DeliveryFallbackException;
import ru.yandex.practicum.interaction.client.feign.order.OrderFallbackException;
import ru.yandex.practicum.interaction.client.feign.payment.PaymentFallbackException;
import ru.yandex.practicum.interaction.client.feign.shopping.cart.ShoppingCartFallbackException;
import ru.yandex.practicum.interaction.client.feign.shopping.store.ShoppingStoreFallbackException;
import ru.yandex.practicum.interaction.client.feign.warehouse.WarehouseFallbackException;
import ru.yandex.practicum.interaction.exception.delivery.DeliveryChangeStateException;
import ru.yandex.practicum.interaction.exception.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.interaction.exception.order.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.order.OrderChangeStateException;
import ru.yandex.practicum.interaction.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.interaction.exception.payment.PaymentChangeStateException;
import ru.yandex.practicum.interaction.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.exception.shopping.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.exception.shopping.cart.ShoppingCartDeactivateException;
import ru.yandex.practicum.interaction.exception.shopping.store.ProductNotFoundException;
import ru.yandex.practicum.interaction.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exception.warehouse.OrderBookingNotFoundException;
import ru.yandex.practicum.interaction.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.interaction.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException cause) {
        String userMessage = "Нарушение валидации аргумента метода";
        String httpStatus = String.valueOf(HttpStatus.BAD_REQUEST.value());

        log.warn(cause.getMessage(), cause);
        return new ApiErrorResponse(userMessage, httpStatus);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException cause) {
        String userMessage = "Нарушение ограничений (constraint)";
        String httpStatus = String.valueOf(HttpStatus.BAD_REQUEST.value());

        log.warn(userMessage, cause);
        return new ApiErrorResponse(userMessage, httpStatus);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({
            ShoppingCartFallbackException.class,
            ShoppingStoreFallbackException.class,
            PaymentFallbackException.class,
            OrderFallbackException.class,
            DeliveryFallbackException.class,
            WarehouseFallbackException.class
    })
    public ApiErrorResponse handleAllFallbackException(RuntimeException cause) {
        String userMessage = cause.getMessage();
        String httpStatus = String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value());

        log.warn(userMessage, cause);
        return new ApiErrorResponse(userMessage, httpStatus);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedUserException.class)
    public ApiErrorResponse handleNotAuthorizedUserException(NotAuthorizedUserException cause) {
        log.warn("Пользователь не авторизован", cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderBookingNotFoundException.class)
    public ApiErrorResponse handleOrderBookingNotFoundException(OrderBookingNotFoundException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrderChangeStateException.class)
    public ApiErrorResponse handleOrderChangeStateException(OrderChangeStateException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoOrderFoundException.class)
    public ApiErrorResponse handleNoOrderFoundException(NoOrderFoundException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaymentChangeStateException.class)
    public ApiErrorResponse handlePaymentChangeStateException(PaymentChangeStateException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    public ApiErrorResponse handleNotEnoughInfoInOrderToCalculateException(
            NotEnoughInfoInOrderToCalculateException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoDeliveryFoundException.class)
    public ApiErrorResponse handleNoDeliveryFoundException(NoDeliveryFoundException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DeliveryChangeStateException.class)
    public ApiErrorResponse handleDeliveryChangeStateException(DeliveryChangeStateException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ShoppingCartDeactivateException.class)
    public ApiErrorResponse handleShoppingCartDeactivateException(ShoppingCartDeactivateException cause) {
        log.warn("Корзина деактивирована", cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ApiErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException cause) {
        log.warn("Нет искомых товаров в корзине", cause);
        return new ApiErrorResponse(cause);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleProductNotFoundException(ProductNotFoundException e) {
        log.warn(e.getUserMessage(), e);
        return new ApiErrorResponse(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ApiErrorResponse handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    public ApiErrorResponse handleProductInShoppingCartLowQuantityInWarehouseException(
            ProductInShoppingCartLowQuantityInWarehouseException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ApiErrorResponse handleSpecifiedProductAlreadyInWarehouseException(
            SpecifiedProductAlreadyInWarehouseException cause) {
        log.warn(cause.getUserMessage(), cause);
        return new ApiErrorResponse(cause);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiErrorResponse handleNotFoundException(NotFoundException e) {
        String userMessage = "Ресурс не найден";
        String httpStatus = String.valueOf(HttpStatus.NOT_FOUND.value());

        log.warn(userMessage, e);
        return new ApiErrorResponse(userMessage, httpStatus);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ApiErrorResponse handleInternalServerErrorException(InternalServerErrorException e) {
        String userMessage = "Внутренняя ошибка сервера";
        String httpStatus = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

        log.error(userMessage, e);
        return new ApiErrorResponse(userMessage, httpStatus);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleException(Exception e) {
        String userMessage = "Внутренняя ошибка сервера";
        String httpStatus = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

        log.error(userMessage, e);
        return new ApiErrorResponse(userMessage, httpStatus);
    }
}
