package ru.yandex.practicum.interaction.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductDto;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductPageDto;
import ru.yandex.practicum.interaction.dto.shopping.store.SetProductQuantityStateRequest;

import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClientFeign {
    // Получение списка товаров по типу в пагинированном виде
    @GetMapping("/api/v1/shopping-store")
    ProductPageDto getProductsByCategory(ProductCategory category, Pageable pageable);

    // Создание нового товара в ассортименте
    @PutMapping("/api/v1/shopping-store")
    ProductDto createProduct(ProductDto productDto);

    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(ProductDto productDto);

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    Boolean deleteProduct(UUID productId);

    // Установка статуса по товару. API вызывается со стороны склада.
    @PostMapping("/api/v1/shopping-store/quantityState")
    Boolean setProductQuantityState(SetProductQuantityStateRequest request);

    // Получить сведения по товару из БД.
    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(UUID productId);
}
