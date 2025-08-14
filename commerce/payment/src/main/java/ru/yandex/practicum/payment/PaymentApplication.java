package ru.yandex.practicum.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.interaction",
        "ru.yandex.practicum.payment",
        "ru.yandex.practicum.logging"})
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
