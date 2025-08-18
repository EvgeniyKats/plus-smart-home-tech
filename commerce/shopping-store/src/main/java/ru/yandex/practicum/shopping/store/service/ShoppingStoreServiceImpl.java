package ru.yandex.practicum.shopping.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductDto;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductPageDto;
import ru.yandex.practicum.interaction.dto.shopping.store.ProductState;
import ru.yandex.practicum.interaction.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.exception.shopping.store.ProductNotFoundException;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.shopping.store.mapper.ProductMapper;
import ru.yandex.practicum.shopping.store.model.Product;
import ru.yandex.practicum.shopping.store.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Logging(Level.TRACE)
    public ProductPageDto getProductsByCategory(ProductCategory category, Pageable pageable) {
        // поиск только всех товаров
        List<Product> products = productRepository.findByProductCategory(category, pageable);

        List<ProductDto> productsDto = products.stream()
                .map(productMapper::toProductDto)
                .toList();

        return new ProductPageDto(productsDto, pageable.getSort());
    }

    @Override
    @Logging(Level.TRACE)
    public Map<UUID, BigDecimal> getProductsPrice(Collection<UUID> productIdsToGetPrice) {
        List<Product> productsFound = productRepository.findAllById(productIdsToGetPrice);

        // проверяем, все ли товары нашлись в БД
        if (productsFound.size() != productIdsToGetPrice.size()) {
            // notFoundIds изначально содержит все запрошенные id, затем удаляются найденные id
            Set<UUID> notFoundIds = new HashSet<>(productIdsToGetPrice);

            productsFound.forEach(product -> notFoundIds.remove(product.getProductId()));

            throw new ProductNotFoundException(notFoundIds);
        }

        return productsFound.stream().collect(Collectors.toMap(Product::getProductId, Product::getPrice));
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        productRepository.save(product);

        return productMapper.toProductDto(product);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public ProductDto updateProduct(ProductDto productDto) {
        // проверка на наличие в БД
        UUID productId = productDto.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        // обновление
        productMapper.updateProductFromDto(product, productDto);

        return productMapper.toProductDto(product);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public Boolean deleteProduct(UUID productId) {
        // проверка на наличие в БД
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        // если уже DEACTIVATE, вернуть false
        if (product.getProductState().equals(ProductState.DEACTIVATE)) {
            return false;
        }

        product.setProductState(ProductState.DEACTIVATE);
        return true;
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public Boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        // проверка на наличие в БД
        UUID productId = request.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        // установка количества
        product.setQuantityState(request.getQuantityState());

        return true;
    }

    @Override
    @Logging(Level.TRACE)
    public ProductDto getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        return productMapper.toProductDto(product);
    }
}
