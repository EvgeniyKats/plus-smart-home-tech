package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.util.HubEventHandleFactory;
import ru.yandex.practicum.util.SensorEventHandleFactory;

@RequiredArgsConstructor
@Service
public class EventCollectorServiceImpl implements EventCollectorService {
    private final SensorEventHandleFactory sensorEventHandleFactory;
    private final HubEventHandleFactory hubEventHandleFactory;

    @Override
    public void collectHubEvent(BaseHubEvent event) {
        hubEventHandleFactory.getHubEventHandlerByType(event.getType()).handle(event);

    }

    @Override
    public void collectSensorEvent(BaseSensorEvent event) {
        sensorEventHandleFactory.getSensorEventHandlerByType(event.getType()).handle(event);
    }
}
