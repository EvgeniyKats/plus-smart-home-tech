package ru.yandex.practicum.warehouse.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.feign.order.OrderClientFeign;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exception.warehouse.OrderBookingNotFoundException;
import ru.yandex.practicum.interaction.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.interaction.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.interaction.util.ProductNotEnough;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.warehouse.mapper.AddressMapper;
import ru.yandex.practicum.warehouse.mapper.ProductMapper;
import ru.yandex.practicum.warehouse.model.Address;
import ru.yandex.practicum.warehouse.model.Dimension;
import ru.yandex.practicum.warehouse.model.OrderBooking;
import ru.yandex.practicum.warehouse.model.Product;
import ru.yandex.practicum.warehouse.repository.AddressRepository;
import ru.yandex.practicum.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.warehouse.repository.ProductRepository;
import ru.yandex.practicum.warehouse.service.param.ResultCheckWarehouseProductsQuantity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductRepository productRepository;
    private final OrderBookingRepository orderBookingRepository;
    private final AddressRepository addressRepository;
    private final ProductMapper productMapper;
    private final AddressMapper addressMapper;

    private final OrderClientFeign orderClientFeign;

    // инициализируется тестовыми данными при создании склада
    private final UUID addressId;

    public WarehouseServiceImpl(ProductRepository productRepository,
                                OrderBookingRepository orderBookingRepository,
                                AddressRepository addressRepository,
                                ProductMapper productMapper,
                                AddressMapper addressMapper,
                                OrderClientFeign orderClientFeign) {
        this.productRepository = productRepository;
        this.orderBookingRepository = orderBookingRepository;
        this.addressRepository = addressRepository;
        this.productMapper = productMapper;
        this.addressMapper = addressMapper;

        this.orderClientFeign = orderClientFeign;

        String[] address = {"ADDRESS_1", "ADDRESS_2"};
        int randomIdx = Random.from(new SecureRandom()).nextInt(0, address.length);
        this.addressId = addressRepository.save(Address.createTestAddress(address[randomIdx])).getId();
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void newProduct(NewProductInWarehouseRequest newRequest) {
        UUID productId = newRequest.getProductId();

        // проверка, есть ли такой товар уже на складе
        productRepository.findById(productId).ifPresent(product -> {
            log.warn("Товар с id={} уже зарегистрирован", productId);
            throw new SpecifiedProductAlreadyInWarehouseException();
        });

        Product product = productMapper.toProduct(newRequest);
        productRepository.save(product);

        log.trace("start newProduct newRequest={}, productId={}", newRequest, product.getProductId());
    }

    @Override
    @Logging(Level.TRACE)
    public BookedProductsDto checkProducts(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> productsToCheck = shoppingCartDto.getProducts();
        return checkWarehouseProductsQuantity(productsToCheck).getBookedProductsDto();
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void addProduct(AddProductToWarehouseRequest addRequest) {
        UUID productId = addRequest.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Нет информации о товаре на складе productId={}", productId);
                    return new NoSpecifiedProductInWarehouseException(List.of(productId));
                });

        Long currentQuantity = product.getQuantity();
        Long addQuantity = addRequest.getQuantity();
        Long newQuantity = currentQuantity + addQuantity;

        product.setQuantity(newQuantity);
        log.trace("end addProduct addRequest={}, newQuantity={}", addRequest, newQuantity);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void shipped(ShippedToDeliveryRequest shippedToDeliveryRequest) {
        // ищем заказ в БД
        UUID orderId = shippedToDeliveryRequest.getOrderId();
        OrderBooking orderBooking = orderBookingRepository.findById(orderId)
                .orElseThrow(() -> new OrderBookingNotFoundException(orderId));

        // заполняем идентификатор доставки
        UUID deliveryId = shippedToDeliveryRequest.getDeliveryId();
        orderBooking.setDeliveryId(deliveryId);
        log.trace("Сведения о заказе {} успешно обновлены", orderId);
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public void returnProducts(Map<UUID, Long> productsToReturn) {
        // отображение идентификатора товара на 0 количество, для проверки "существования" товаров в БД
        Map<UUID, Long> productsToCheck = productsToReturn.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), id -> 0L));
        ResultCheckWarehouseProductsQuantity resultCheck = checkWarehouseProductsQuantity(productsToCheck);

        // после проверки увеличиваем количество товаров на складе
        resultCheck.getProducts().values().forEach(product ->
                product.setQuantity(product.getQuantity() + productsToReturn.get(product.getProductId())));

        log.trace("Успешное увеличение количества товаров после возврата");
    }

    @Override
    @Transactional
    @Logging(Level.TRACE)
    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest) {
        UUID orderId = assemblyProductsForOrderRequest.getOrderId();
        Map<UUID, Long> productsToAssembly = assemblyProductsForOrderRequest.getProducts();

        // проверка наличия на складе
        ResultCheckWarehouseProductsQuantity resultCheck;

        try {
            resultCheck = checkWarehouseProductsQuantity(productsToAssembly);
        } catch (NoSpecifiedProductInWarehouseException | ProductInShoppingCartLowQuantityInWarehouseException e) {
            orderClientFeign.setAssemblyFailed(orderId);
            throw e;
        }

        // после проверки уменьшаем количество товаров на складе
        resultCheck.getProducts().values().forEach(product ->
                product.setQuantity(product.getQuantity() - productsToAssembly.get(product.getProductId())));

        // заполняем сведения о забронированном количестве товаров для заказа
        OrderBooking orderBooking = OrderBooking.builder()
                .orderId(orderId)
                .bookedProducts(productsToAssembly)
                .build();
        orderBookingRepository.save(orderBooking);

        // устанавливаем статус успешной сборки
        orderClientFeign.setAssemblySuccess(orderId);

        return resultCheck.getBookedProductsDto();
    }

    /**
     * @implNote Сейчас возвращаются временные данные
     */
    @Override
    @Logging(Level.TRACE)
    public AddressDto getAddress() {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalStateException("Адрес не найден в БД, id = " + addressId));
        return addressMapper.toAddressDto(address);
    }

    /**
     * Принимает мапу товаров и проверяет их наличие на складе
     *
     * @param products - Отображение идентификатора товара на отобранное количество.
     * @return - возвращает заполненный BookedProductsDto и товары из БД
     * @throws NoSpecifiedProductInWarehouseException               - если есть товары, информации о которых нет на
     *                                                              складе
     * @throws ProductInShoppingCartLowQuantityInWarehouseException - если есть товары, количество которых на складе
     *                                                              недостаточно
     */
    private ResultCheckWarehouseProductsQuantity checkWarehouseProductsQuantity(Map<UUID, Long> products) {
        // Загружаем из БД товары для проверки
        Set<UUID> ids = products.keySet();
        Map<UUID, Product> productById = productRepository.findAllAsMapByIds(ids);

        // Начинаем проверку.
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder() // общая информация о товарах для заказа
                .deliveryVolume(0.0)
                .deliveryWeight(0.0)
                .fragile(false)
                .build();

        List<ProductNotEnough> productsNotEnough = new ArrayList<>(); // товары, которых недостаточно на складе.
        List<UUID> productsNotFound = new ArrayList<>(); // товары, которых нет в БД склада.

        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            UUID id = entry.getKey();
            Long wantedCount = entry.getValue();

            if (!productById.containsKey(id)) {
                productsNotFound.add(id);
                continue;
            }

            // Товар из БД
            Product product = productById.get(id);

            Long availableCount = product.getQuantity();

            if (wantedCount > availableCount) {
                productsNotEnough.add(new ProductNotEnough(id, availableCount, wantedCount));
                continue;
            }

            // Если товар есть на складе и его достаточное количество, заполняем результат
            Dimension dimension = product.getDimension();

            // Объем
            Double currentVolume = bookedProductsDto.getDeliveryVolume();
            Double addVolume = dimension.getHeight() * dimension.getWidth() * dimension.getDepth();
            Double newVolume = currentVolume + addVolume;
            bookedProductsDto.setDeliveryVolume(newVolume);

            // Вес
            Double currentWeight = bookedProductsDto.getDeliveryWeight();
            Double addWeight = product.getWeight() * wantedCount;
            Double newWeight = currentWeight + addWeight;
            bookedProductsDto.setDeliveryWeight(newWeight);

            // Признак хрупкости, если хотя бы 1 товар хрупкий, заказ считается хрупким
            if (product.getFragile() != null) {
                boolean fragile = bookedProductsDto.isFragile() || product.getFragile(); // true true -> t, tf->t,  ff->f
                bookedProductsDto.setFragile(fragile);
            }
        }

        // если есть товары, информации о которых нет на складе
        if (!productsNotFound.isEmpty()) {
            log.warn("Нет информации о товарах на складе ID: {}", productsNotFound);
            throw new NoSpecifiedProductInWarehouseException(productsNotFound);
        }

        // если есть товары, количество которых на складе недостаточно
        if (!productsNotEnough.isEmpty()) {
            log.warn("Недостаточно товаров на складе: {}", productsNotEnough);
            throw new ProductInShoppingCartLowQuantityInWarehouseException(productsNotEnough);
        }

        return ResultCheckWarehouseProductsQuantity.builder()
                .products(productById)
                .bookedProductsDto(bookedProductsDto)
                .build();
    }
}
