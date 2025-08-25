package ru.yandex.practicum.delivery.util.calculate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.delivery.config.DeliveryConfig;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.util.calculate.param.CalculateDeliveryCostParam;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {DeliveryCalculateImpl.class})
@EnableConfigurationProperties(DeliveryConfig.class)
class DeliveryCalculateTest {

    @Autowired
    DeliveryCalculate deliveryCalculate;

    @Autowired
    DeliveryConfig deliveryConfig;

    @Test
    void shouldHaveFilledFields_DeliveryConfig() {
        assertNotNull(deliveryConfig);
        DeliveryConfig.DeliveryValues values = deliveryConfig.getDeliveryValues();
        assertNotNull(values);
        assertNotNull(values.getBaseCost());
        assertNotNull(values.getFragileMultiply());
        assertNotNull(values.getDifferentStreetMultiply());
        assertNotNull(values.getVolumeMultiply());
        assertNotNull(values.getWeightMultiply());
        assertNotNull(values.getWarehouseAddress1Multiply());
        assertNotNull(values.getWarehouseAddress2Multiply());
    }

    @Test
    void calculateDeliveryCost() {
        Address addressFrom = Address.builder()
                .city("ADDRESS_2")
                .street("ADDRESS_2")
                .build();

        Address addressTo = Address.builder()
                .street("Пролетарская")
                .house("31")
                .build();


        CalculateDeliveryCostParam calculateDeliveryCostParam = CalculateDeliveryCostParam.builder()
                .fromAddress(addressFrom)
                .toAddress(addressTo)
                .weight(new BigDecimal("10"))
                .volume(new BigDecimal("10"))
                .fragile(true)
                .build();

        BigDecimal deliveryCost = deliveryCalculate.calculateDeliveryCost(calculateDeliveryCostParam);
        BigDecimal expected = new BigDecimal("27.60");
        assertEquals(expected, deliveryCost);
    }
}