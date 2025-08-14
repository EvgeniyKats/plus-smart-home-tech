package ru.yandex.practicum.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.interaction",
        "ru.yandex.practicum.delivery",
        "ru.yandex.practicum.logging"})
public class DeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }
}
