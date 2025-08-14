package ru.yandex.practicum.shopping.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.interaction",
        "ru.yandex.practicum.shopping.store",
        "ru.yandex.practicum.logging"})
public class ShoppingStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingStoreApplication.class, args);
    }
}
