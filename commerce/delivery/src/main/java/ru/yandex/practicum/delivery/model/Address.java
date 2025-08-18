package ru.yandex.practicum.delivery.model;

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

import java.util.UUID;

/**
 * Представление адреса в БД
 */

// lombok annotations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// JPA annotations
@Entity
@Table(name = "delivery_address")
public class Address {

    // Идентификатор адреса.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    // Страна
    @Column(name = "country", nullable = false)
    String country;

    // Город
    @Column(name = "city", nullable = false)
    String city;

    // Улица
    @Column(name = "street", nullable = false)
    String street;

    // Дом
    @Column(name = "house", nullable = false)
    String house;

    // Квартира
    @Column(name = "flat", nullable = false)
    String flat;
}
