package ru.yandex.practicum.shopping.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.interaction",
        "ru.yandex.practicum.shopping.cart",
        "ru.yandex.practicum.logging"})
public class ShoppingCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }
}
