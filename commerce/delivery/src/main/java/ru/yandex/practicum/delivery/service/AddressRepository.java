package ru.yandex.practicum.delivery.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.delivery.model.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query("""
            SELECT a
            FROM Address a
            WHERE country = :country
            AND city = :city
            AND street = :street
            AND house = :house
            AND flat = :flat
            """)
    Optional<Address> findByAllFields(@Param("country") String country,
                                      @Param("city") String city,
                                      @Param("street") String street,
                                      @Param("house") String house,
                                      @Param("flat") String flat);

    default Address findOrCreateByAllFields(String country,
                                            String city,
                                            String street,
                                            String house,
                                            String flat) {
        return findByAllFields(country, city, street, house, flat)
                .orElseGet(() -> {
                    Address address = Address.builder()
                            .country(country)
                            .city(city)
                            .street(street)
                            .house(house)
                            .flat(flat)
                            .build();
                    save(address);
                    return address;
                });
    }
}
