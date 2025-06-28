package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.util.HubEventHandleFactory;
import ru.yandex.practicum.util.SensorEventHandleFactory;

@RequiredArgsConstructor
@Service
public class EventCollectorServiceImpl implements EventCollectorService {
    private static final Logger log = LoggerFactory.getLogger(EventCollectorServiceImpl.class);
    private final SensorEventHandleFactory sensorEventHandleFactory;
    private final HubEventHandleFactory hubEventHandleFactory;

    @Override
    public void collectHubEvent(BaseHubEvent event) {
        log.info("collect hub: ev.type={}, ev.hubId={}, ev.timestamp={}",
                event.getType(),
                event.getHubId(),
                event.getTimestamp());
        hubEventHandleFactory.getHubEventHandlerByType(event.getType()).handle(event);
    }

    @Override
    public void collectSensorEvent(BaseSensorEvent event) {
        log.info("collect sensor: ev.type={}, ev.hubId={}, ev.timestamp={}",
                event.getType(),
                event.getHubId(),
                event.getTimestamp());
        sensorEventHandleFactory.getSensorEventHandlerByType(event.getType()).handle(event);
    }
}
