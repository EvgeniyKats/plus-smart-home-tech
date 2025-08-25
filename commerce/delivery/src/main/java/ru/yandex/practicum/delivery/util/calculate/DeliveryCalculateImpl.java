package ru.yandex.practicum.delivery.util.calculate;

import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.delivery.config.DeliveryConfig;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.util.calculate.param.CalculateDeliveryCostParam;
import ru.yandex.practicum.logging.Logging;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Validated
public class DeliveryCalculateImpl implements DeliveryCalculate {
    private static final String ADDRESS_1 = "ADDRESS_1";
    private static final String ADDRESS_2 = "ADDRESS_2";

    private final DeliveryConfig.DeliveryValues deliveryValues;

    public DeliveryCalculateImpl(DeliveryConfig deliveryConfig) {
        deliveryValues = deliveryConfig.getDeliveryValues();
    }

    @Override
    public BigDecimal calculateDeliveryCost(CalculateDeliveryCostParam param) {
        // инициализация базовой стоимостью
        BigDecimal deliveryCost = deliveryValues.getBaseCost();

        // вычисление добавочной стоимости в зависимости от адреса склада
        BigDecimal warehouseCost = calculateAddressWarehouseCost(param.getFromAddress(), deliveryCost);
        deliveryCost = deliveryCost.add(warehouseCost);

        // вычисление добавочной стоимости хрупкости
        BigDecimal fragileCost = calculateFragileCost(param.getFragile(), deliveryCost);
        deliveryCost = deliveryCost.add(fragileCost);

        // вычисление добавочной стоимости веса
        BigDecimal weightCost = calculateWeightCost(param.getWeight());
        deliveryCost = deliveryCost.add(weightCost);

        // вычисление добавочной стоимости объема
        BigDecimal volumeCost = calculateVolumeCost(param.getVolume());
        deliveryCost = deliveryCost.add(volumeCost);

        // вычисление добавочной стоимости отдаленной улицы
        String streetFrom = param.getFromAddress().getStreet();
        String streetTo = param.getToAddress().getStreet();
        BigDecimal differentStreetCost = calculateDifferentStreetCost(streetFrom, streetTo, deliveryCost);

        return deliveryCost.add(differentStreetCost).setScale(2, RoundingMode.DOWN);
    }

    @Logging(Level.DEBUG)
    private BigDecimal calculateAddressWarehouseCost(Address addressWarehouse, BigDecimal before) {
        BigDecimal addressAddCost = BigDecimal.ZERO;

        if (addressWarehouse.getCity().equals(ADDRESS_1)) {
            addressAddCost = before.multiply(deliveryValues.getWarehouseAddress1Multiply());
        } else if (addressWarehouse.getCity().equals(ADDRESS_2)) {
            addressAddCost = before.multiply(deliveryValues.getWarehouseAddress2Multiply());
        }

        return addressAddCost;
    }

    @Logging(Level.DEBUG)
    private BigDecimal calculateFragileCost(boolean fragile, BigDecimal before) {
        return fragile
                ? before.multiply(deliveryValues.getFragileMultiply())
                : BigDecimal.ZERO;
    }

    @Logging(Level.DEBUG)
    private BigDecimal calculateWeightCost(BigDecimal weight) {
        return weight.multiply(deliveryValues.getWeightMultiply());
    }

    @Logging(Level.DEBUG)
    private BigDecimal calculateVolumeCost(BigDecimal volume) {
        return volume.multiply(deliveryValues.getVolumeMultiply());
    }

    @Logging(Level.DEBUG)
    private BigDecimal calculateDifferentStreetCost(String streetFrom, String streetTo, BigDecimal before) {
        return streetFrom.equals(streetTo)
                ? BigDecimal.ZERO
                : before.multiply(deliveryValues.getDifferentStreetMultiply());
    }
}