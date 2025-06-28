package ru.yandex.practicum.service;

import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;

public interface EventCollectorService {
    void collectHubEvent(BaseHubEvent event);

    void collectSensorEvent(BaseSensorEvent event);
}
