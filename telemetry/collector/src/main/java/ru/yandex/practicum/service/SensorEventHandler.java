package ru.yandex.practicum.service;

import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getSensorEventType();

    void handle(BaseSensorEvent sensorEvent);
}
