package ru.yandex.practicum.shopping.cart.config;

import feign.Feign;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.shopping.cart.feign.FeignErrorDecoder;

@Configuration
@EnableFeignClients(basePackages = {"ru.yandex.practicum.interaction.client.feign"})
public class FeignConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new FeignErrorDecoder());
    }
}
