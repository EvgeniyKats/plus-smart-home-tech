package ru.yandex.practicum.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.exception.shopping.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.exception.shopping.cart.ShoppingCartDeactivateException;
import ru.yandex.practicum.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.model.ShoppingCartStatus;
import ru.yandex.practicum.shopping.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.shopping.cart.util.UsernameValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional
    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        log.trace("start getShoppingCart username={}", username);
        validateUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCartByUsername(username);

        log.trace("end getShoppingCart username={}", username);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto addProductsToShoppingCart(Map<UUID, Integer> products, String username) {
        log.trace("start addProductsToShoppingCart username={}, products={}", username, products);
        validateUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCartByUsername(username);

        validateShoppingCartModifiable(shoppingCart);

        // добавление продуктов в корзину
        products.forEach((id, count) -> shoppingCart.getProducts().merge(id, count, Integer::sum));

        log.trace("end addProductsToShoppingCart username={}, products={}", username, products);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Transactional
    @Override
    public void deactivateShoppingCart(String username) {
        log.trace("start deactivateShoppingCart username={}", username);
        validateUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCartByUsername(username);

        // деактивация корзины
        shoppingCart.setStatus(ShoppingCartStatus.DEACTIVATE);
        log.trace("end deactivateShoppingCart username={}", username);
    }

    @Transactional
    @Override
    public ShoppingCartDto removeProductsFromShoppingCart(List<UUID> productsIds, String username) {
        log.trace("start removeProductsFromShoppingCart username={}, productsIds={}", username, productsIds);
        validateUsername(username);

        ShoppingCart shoppingCart = getOrCreateShoppingCartByUsername(username);
        // проверка, все ли товары для удаления есть в корзине
        validateShoppingCartHaveAllProductIds(shoppingCart, productsIds);

        // удаление товаров
        productsIds.forEach(id -> shoppingCart.getProducts().remove(id));

        log.trace("end removeProductsFromShoppingCart username={}, productsIds={}", username, productsIds);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto changeProductsQuantityInShoppingCart(Map<UUID, Integer> products, String username) {
        log.trace("start changeProductsQuantityInShoppingCart username={}, products={}", username, products);
        validateUsername(username);

        ShoppingCart shoppingCart = getOrCreateShoppingCartByUsername(username);
        // проверка, все ли товары для изменения количества есть в корзине
        validateShoppingCartHaveAllProductIds(shoppingCart, products.keySet());

        // изменение товаров
        products.forEach((id, count) -> shoppingCart.getProducts().put(id, count));

        log.trace("end changeProductsQuantityInShoppingCart username={}, products={}", username, products);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    /**
     * Если корзина для пользователя существует в БД, вернёт существующую
     * или создаст новую пустую корзину и сохранит её в БД
     */
    private ShoppingCart getOrCreateShoppingCartByUsername(String username) {
        return shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> {

                    ShoppingCart cart = ShoppingCart.builder()
                            .username(username)
                            .build();
                    shoppingCartRepository.save(cart);

                    log.trace("Создана новая корзина id={}, username={}", cart.getShoppingCartId(), cart.getUsername());
                    return cart;
                });
    }

    /**
     * Проверяет, является ли username пустым (или равен null)
     * @throws NotAuthorizedUserException, если username пустой
     */
    private void validateUsername(String username) {
        if (!UsernameValidator.isUsernameValid(username)) {
            log.warn("username={} имя пользователя не должно быть пустым", username);
            throw new NotAuthorizedUserException();
        }
        log.debug("Имя пользователя прошло валидацию username={}", username);
    }

    /**
     * Проверяет корзину на возможность модификации
     * @throws ShoppingCartDeactivateException , если корзина деактивирована (её статус DEACTIVATE)
     */
    private void validateShoppingCartModifiable(ShoppingCart shoppingCart) {
        if (shoppingCart.getStatus().equals(ShoppingCartStatus.DEACTIVATE)) {
            log.warn("корзина пользователя деактивирована username={}", shoppingCart.getUsername());
            throw new ShoppingCartDeactivateException();
        }
        log.trace("корзина пользователя username={} успешно прошла валидацию для модификации",
                shoppingCart.getUsername());
    }

    /**
     * Проверяет наличие всех id товаров в корзине
     *
     * @param shoppingCart - корзина с товарами
     * @param productsIds  - id товаров, которые должны быть в корзине
     * @throws NoProductsInShoppingCartException если в корзине отсутствуют один или несколько товаров из списка productsIds
     * @implNote time: O(n), n - размер productsIds, mem: O(n) - размер notFoundIds = в худшем случае productsIds
     */
    private void validateShoppingCartHaveAllProductIds(ShoppingCart shoppingCart, Collection<UUID> productsIds) {
        // если количество удаляемых товаров > количество товаров в корзине, возникает несоответствие
        int countProductInCart = shoppingCart.getProducts().size();
        int countProductToRemove = productsIds.size();

        if (countProductToRemove > countProductInCart) {
            log.warn("Количество удаляемых товаров больше чем товаров в корзине countProductInCart={}, countProductToRemove={}",
                    countProductInCart,
                    countProductToRemove);
            throw new NoProductsInShoppingCartException();
        }

        // notFoundIds содержит не найденные id
        List<UUID> notFoundIds = new ArrayList<>();

        // поиск отсутствующих товаров
        productsIds.forEach(id -> {
            if (!shoppingCart.getProducts().containsKey(id)) {
                notFoundIds.add(id);
            }
        });

        // если notFoundIds не пустой, значит в корзине отсутствуют товары для удаления
        if (!notFoundIds.isEmpty()) {
            log.warn("Обнаружены товары, которых нет в корзине notFoundIds={}", notFoundIds);
            throw new NoProductsInShoppingCartException();
        }
        log.trace("success validate, cart={} have all products", shoppingCart.getShoppingCartId());
    }

}
