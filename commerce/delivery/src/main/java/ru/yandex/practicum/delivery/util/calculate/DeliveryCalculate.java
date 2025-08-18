package ru.yandex.practicum.delivery.util.calculate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.event.Level;
import ru.yandex.practicum.delivery.util.calculate.param.CalculateDeliveryCostParam;
import ru.yandex.practicum.logging.Logging;

import java.math.BigDecimal;

public interface DeliveryCalculate {
    /**
     * Рассчитывает стоимость доставки
     *
     * @param param Содержит данные для расчёта стоимости доставки
     * @return Стоимость доставки
     * @throws jakarta.validation.ConstraintViolationException Если параметр не прошел валидацию
     */
    @Logging(Level.DEBUG)
    BigDecimal calculateDeliveryCost(@NotNull @Valid CalculateDeliveryCostParam param);
}
