package ru.yandex.practicum.util;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.sensor.SensorEventType;
import ru.yandex.practicum.service.SensorEventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class SensorEventHandleFactory {
    private final Map<SensorEventType, SensorEventHandler> sensorHandlers;

    public SensorEventHandleFactory(Set<SensorEventHandler> handlers) {
        sensorHandlers = new HashMap<>();
        handlers.forEach(handler -> sensorHandlers.put(handler.getSensorEventType(), handler));
    }

    public SensorEventHandler getSensorEventHandlerByType(SensorEventType sensorEventType) {
        return Optional.ofNullable(sensorHandlers.get(sensorEventType))
                .orElseThrow(() -> new NotFoundException(sensorEventType + " not exist in map"));
    }
}
