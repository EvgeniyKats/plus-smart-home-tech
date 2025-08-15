package ru.yandex.practicum.order.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Обеспечивает мгновенную выгрузку заказов из БД
    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.products.products
            WHERE o.shoppingCartId IN :cartIds
            """)
    List<Order> findAllByShoppingCartIdWithProducts(@Param("cartIds") List<UUID> cartIds, Pageable pageable);

    // Обеспечивает ленивую выгрузку заказов из БД
    List<Order> findAllByShoppingCartIdIn(List<UUID> cartIds, Pageable pageable);
}
