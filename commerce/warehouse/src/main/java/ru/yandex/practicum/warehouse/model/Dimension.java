package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Размеры товара
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// JPA annotations
@Entity
@Table(name = "dimension")
public class Dimension {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    // Ширина
    @Column(name = "width", scale = 3, precision = 10)
    BigDecimal width;

    // Высота
    @Column(name = "height", scale = 3, precision = 10)
    BigDecimal height;

    // Глубина
    @Column(name = "depth", scale = 3, precision = 10)
    BigDecimal depth;
}
