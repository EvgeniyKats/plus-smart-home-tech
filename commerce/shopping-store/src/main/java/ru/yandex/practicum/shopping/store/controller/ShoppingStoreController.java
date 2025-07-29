package ru.yandex.practicum.shopping.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.dto.shopping.store.Pageable;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductDto;
import ru.yandex.practicum.interaction.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.shopping.store.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreController {
    private final ShoppingStoreService shoppingStoreService;

    // Получение списка товаров по типу в пагинированном виде
    @GetMapping
    public List<ProductDto> getProductsByCategory(@RequestParam ProductCategory category,
                                                  @RequestParam Pageable pageable) {
        log.info("start getProductsByCategory category={}, pageable={}", category, pageable);

        List<ProductDto> result = shoppingStoreService.getProductsByCategory(category, pageable);
        log.debug("getProductsByCategory result={}", result);

        log.info("success getProductsByCategory category={}, pageable={}", category, pageable);
        return result;
    }

    // Создание нового товара в ассортименте
    @PutMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("start createProduct productDto={}", productDto);

        ProductDto result = shoppingStoreService.createProduct(productDto);
        log.debug("createProduct result={}", result);

        log.info("success createProduct productDto={}", productDto);
        return result;
    }


    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("start updateProduct productDto={}", productDto);

        ProductDto result = shoppingStoreService.updateProduct(productDto);
        log.debug("updateProduct result={}", result);

        log.info("success updateProduct productDto={}", productDto);
        return result;
    }

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    @PostMapping("/removeProductFromStore")
    public Boolean deleteProduct(@RequestBody UUID productId) {
        log.info("start deleteProduct productId={}", productId);

        Boolean result = shoppingStoreService.deleteProduct(productId);
        log.debug("deleteProduct result={}", result);

        log.info("end deleteProduct productId={}", productId);
        return result;
    }

    // Установка статуса по товару. API вызывается со стороны склада.
    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@Valid @RequestBody SetProductQuantityStateRequest request) {
        log.info("start setProductQuantityState request={}", request);

        Boolean result = shoppingStoreService.setProductQuantityState(request);
        log.debug("setProductQuantityState result={}", result);

        log.info("end setProductQuantityState request={}", request);
        return result;
    }

    // Получить сведения по товару из БД.
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        log.info("start getProduct productId={}", productId);

        ProductDto result = shoppingStoreService.getProduct(productId);
        log.debug("getProduct result={}", result);

        log.info("success getProduct productId={}", productId);
        return result;
    }
}
