package ru.yandex.practicum.payment.util.calculate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.event.Level;
import ru.yandex.practicum.logging.Logging;
import ru.yandex.practicum.payment.util.calculate.param.CalculateProductsCostParam;
import ru.yandex.practicum.payment.util.calculate.param.CalculateTotalCostParam;

import java.math.BigDecimal;

public interface PaymentCalculate {
    /**
     * Вычисляет общую стоимость товаров
     *
     * @param param Параметр с данными для вычислений
     * @return Стоимость товаров
     * @throws IllegalArgumentException                        Если неизвестна стоимость товара
     * @throws jakarta.validation.ConstraintViolationException Если переданный аргумент не пройдет валидацию
     */
    @Logging(Level.DEBUG)
    BigDecimal calculateProductsCost(@NotNull @Valid CalculateProductsCostParam param);

    /**
     * @param param Параметр с данными для вычислений
     * @return Итоговая стоимость
     * @throws jakarta.validation.ConstraintViolationException Если переданный аргумент не пройдет валидацию
     */
    @Logging(Level.DEBUG)
    BigDecimal calculateTotalCost(@NotNull @Valid CalculateTotalCostParam param);

    /**
     * @param productsCost Стоимость продуктов
     * @param feePercent   Процентная ставка налога
     * @return Величина налога
     * @throws jakarta.validation.ConstraintViolationException Если переданные аргументы не пройдут валидацию
     */
    @Logging(Level.DEBUG)
    BigDecimal calculateFee(@NotNull @Positive BigDecimal productsCost,
                            @NotNull @Positive BigDecimal feePercent);
}
