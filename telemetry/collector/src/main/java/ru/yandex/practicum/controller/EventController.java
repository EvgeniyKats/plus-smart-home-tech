package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.service.EventCollectorService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventCollectorService eventCollectorService;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody BaseSensorEvent event) {
        log.info("start POST /events/sensors ev.type={}, ev.hubId={}, ev.timestamp={}",
                event.getType(),
                event.getHubId(),
                event.getTimestamp());
        eventCollectorService.collectSensorEvent(event);
        log.info("end POST /events/sensors ev.type={}, ev.hubId={}, ev.timestamp={}",
                event.getType(),
                event.getHubId(),
                event.getTimestamp());
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody BaseHubEvent event) {
        log.info("start POST /events/hubs ev.type={}, ev.hubId={}, ev.timestamp={}",
                event.getType(),
                event.getHubId(),
                event.getTimestamp());
        log.info("start POST /events/hubs event.type={}", event.getType());
        eventCollectorService.collectHubEvent(event);
        log.info("end POST /events/hubs ev.type={}, ev.hubId={}, ev.timestamp={}",
                event.getType(),
                event.getHubId(),
                event.getTimestamp());
    }
}