package ru.yandex.practicum.shopping.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {
    // Обеспечивает ленивую выгрузку товаров из БД
    Optional<ShoppingCart> findByUsername(String username);

    // Обеспечивает мгновенную выгрузку товаров из БД
    @Query("""
            SELECT cart
            FROM ShoppingCart cart
            LEFT JOIN FETCH cart.products
            WHERE cart.username = :username
            """)
    Optional<ShoppingCart> findByUsernameWithProducts(@Param("username") String username);

    /**
     * Если корзина для пользователя существует в БД, вернёт существующую
     * или создаст новую пустую корзину и сохранит её в БД
     *
     * @param username     - имя пользователя
     * @param withProducts - нужна ли мгновенная выгрузка, true для избегания N + 1
     * @return - найденная или созданная корзина
     */
    default ShoppingCart getOrCreateByUsername(String username, boolean withProducts) {
        Optional<ShoppingCart> shoppingCartOptional = withProducts
                ? findByUsernameWithProducts(username)
                : findByUsername(username);

        return shoppingCartOptional
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = ShoppingCart.builder()
                            .username(username)
                            .build();
                    return save(shoppingCart);
                });
    }
}
