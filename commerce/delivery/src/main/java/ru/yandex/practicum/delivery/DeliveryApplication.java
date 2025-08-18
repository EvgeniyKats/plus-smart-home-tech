package ru.yandex.practicum.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.interaction",
        "ru.yandex.practicum.delivery",
        "ru.yandex.practicum.logging"})
@ConfigurationPropertiesScan
public class DeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }
}
