package ru.yandex.practicum.shopping.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.shopping.store.ShoppingStoreApi;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductDto;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductPageDto;
import ru.yandex.practicum.interaction.dto.shopping.store.QuantityState;
import ru.yandex.practicum.interaction.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.shopping.store.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreController implements ShoppingStoreApi {
    private final ShoppingStoreService shoppingStoreService;

    // Получение списка товаров по типу в пагинированном виде
    @Override
    @Logging
    public ProductPageDto getProductsByCategory(ProductCategory category, Pageable pageable) {
        return shoppingStoreService.getProductsByCategory(category, pageable);
    }

    // Создание нового товара в ассортименте
    @Override
    @Logging
    public ProductDto createProduct(ProductDto productDto) {
        return shoppingStoreService.createProduct(productDto);
    }


    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    @Override
    @Logging
    public ProductDto updateProduct(ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    @Override
    @Logging
    public Boolean deleteProduct(UUID productId) {
        return shoppingStoreService.deleteProduct(productId);
    }

    // Установка статуса по товару. API вызывается со стороны склада.
    @Override
    @Logging
    public Boolean setProductQuantityState(UUID productId,
                                           QuantityState quantityState) {

        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();

        return shoppingStoreService.setProductQuantityState(request);
    }

    // Получить сведения по товару из БД.
    @Override
    @Logging
    public ProductDto getProduct(UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}
