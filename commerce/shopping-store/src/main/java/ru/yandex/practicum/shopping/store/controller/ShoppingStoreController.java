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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreController {
    // Получение списка товаров по типу в пагинированном виде
    @GetMapping
    public List<ProductDto> getProductsByCategory(@RequestParam ProductCategory category,
                                                  @RequestParam Pageable pageable) {
        log.info("start getProductsByCategory category={}, pageable={}", category, pageable);
        // TODO реализовать
        return null;
    }

    // Создание нового товара в ассортименте
    @PutMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("start createProduct productDto={}", productDto);
        // TODO реализовать
        return null;
    }


    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("start updateProduct productDto={}", productDto);
        // TODO реализовать
        return null;
    }

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    @PostMapping("/removeProductFromStore")
    public Boolean deleteProduct(@RequestBody UUID productId) {
        log.info("start deleteProduct productId={}", productId);
        // TODO реализовать
        return null;
    }

    // Установка статуса по товару. API вызывается со стороны склада.
    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@Valid @RequestBody SetProductQuantityStateRequest request) {
        log.info("start setProductQuantityState request={}", request);
        // TODO реализовать
        return null;
    }

    // Получить сведения по товару из БД.
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        log.info("start getProduct ");
        // TODO реализовать
        return null;
    }
}
